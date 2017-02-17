package processes;

import commands.CommandAccess;
import items.MarketScript;
import managers.CommandManager;
import domains.BaseDomain;
import managers.DomainsManager;
import managers.ItemManager;
import managers.Logger;
import objects.*;
import interface_objects.*;

import java.util.*;

public class Worker {
    // region static variables
    public static CommandRequest requestToHandle;
    public static ThreadedJobFactory threadFactory = new ThreadedJobFactory(Parameters.maxWorkerThreads);

    // TODO : add domain manager and command manager, worker is not responsible for them - change the calls
    // endregion

    // region public variables
    public CommandRequest request;
    public String error;
    public List<String> commands = new ArrayList<>();
    public List<Argument> arguments = new ArrayList<>();
    public boolean initCommand = false;
    // endregion

    /**
     * entry point for worker process
     * @param args main program arguments
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        // init the entire system
        initSystem();

        // main loop
        while (true) {
            requestToHandle = null;
            // make sure we have a requestToHandle
            Logger.log("Worker.main", "waiting for a command");
            while (requestToHandle == null)
                requestToHandle = Parser.receiveCommand();

            Logger.log("Worker.main", "got " + requestToHandle.command);
            // create a thread and handle the requestToHandle
            ThreadedJob thread = threadFactory.newThread(new Worker(requestToHandle)::workerStart, null, "workerThread");
            thread.start();
        }
    }

    /**
     * main init method for the entire system
     */
    public static void initSystem() {
        // TODO : add all the initializations and maybe move it somewhere else?
        CommandManager.init();
        DomainsManager.init();
        ItemManager.init();
    }

    /**
     * constructor
     * @param r the request to handle
     */
    public Worker(CommandRequest r) {
        request = r;
    }

    /**
     * entry point for the worker thread to start
     * @param args arguments for the worker thread
     */
    public void workerStart(Object... args) {
        // parse the input and populate commands and arguments
        Logger.log("Worker.workerStart", "got " + request.command);
        if (!checkSyntax(request.command)) {
            if (error.equals(""))
                Parser.addResponse(request.getKey(), error);

            return;
        }

        if (initCommand) {
            HashMap<String, Argument> argsMap = new HashMap<>();
            arguments.forEach((a) -> argsMap.put(a.name, a));
            // asked for auto complete list
            if (argsMap.get(Parameters.InitCommandAutoCompleteList) != null)
                executeInitCommandAutoCompleteList();
            // asked for system spec
            else if (argsMap.get(Parameters.InitCommandSystemSpec) != null)
                executeInitCommandSystemSpec();
            // asked for account balance
            else if (argsMap.get(Parameters.InitCommandAccountBalance) != null)
                executeInitCommandAccountBalance();
            // asked for system status
            else if (argsMap.get(Parameters.InitCommandSystemStatus) != null)
                executeInitCommandSystemStatus();
            // asked for macros
            else if (argsMap.get(Parameters.InitCommandMacros) != null)
                executeInitCommandMacros();

            return;
        }

        String response = null;
        Command commandToRun;

        // TODO : check the macros
        if (request.command.startsWith("/")) {
             ActiveUser activeUser = LoginHandler.getActiveUserByUsername(request.context.username);
             if (activeUser == null) {
                 Parser.addResponse(request.getKey(), Parser.encodeArgument("response", Parameters.ErrorActiveUserNotFound));
                 return;
             }
             HashMap<String, String> macros = activeUser.getMacros();
             if (macros != null) {
                String commandReplacement = macros.get(request.command.substring(1));
                if (commandReplacement == null) {
                    Parser.addResponse(request.getKey(), Parser.encodeArgument("response", Parameters.ErrorMacroNotFound));
                    return;
                }

                request.command = commandReplacement;
             }
        }

        // search in domain specific commands (based on current location)
        BaseDomain location = DomainsManager.getDomainByName(request.context.location);
        if (location != null) {
            commandToRun = parseCommand(location.commands);
            if (commandToRun != null) {
                response = location.executeCommand(request.context, commandToRun.name, arguments);
                Parser.addResponse(request.getKey(), Parser.encodeArgument("response", response));
                return;
            }
        }

        // search in all accessible commands
        commandToRun = parseCommand();
        if (commandToRun != null) {
            response = commandToRun.execute(request.context, commandToRun.name, arguments);
            Parser.addResponse(request.getKey(), Parser.encodeArgument("response", response));
            return;
        }

        // search in market scripts
        MarketScript script = ItemManager.getMarketScriptByName(commands);
        if (script != null) {
            commandToRun = script.command;
            response = commandToRun.execute(request.context, commandToRun.name, arguments);
            Parser.addResponse(request.getKey(), Parser.encodeArgument("response", response));
            return;
        }

        // default fall back, did not find a matching command
        response = Parameters.ErrorCommandDoesNotExists;
        Parser.addResponse(request.getKey(), Parser.encodeArgument("response", response));
    }

    /**
     * executes init command for requesting macros
     */
    public void executeInitCommandMacros() {
        ActiveUser activeUser = LoginHandler.getActiveUserByUsername(request.context.username);
        if (activeUser == null) {
            Parser.addResponse(request.getKey(), Parser.encodeArgument("response", Parameters.ErrorActiveUserNotFound));
            return;
        }

        HashMap<String, String> macros = activeUser.getMacros();
        Parser.addResponse(request.getKey(), Parser.encodeArgumentList(macros));
    }

    /**
     * executes init command for requesting system status
     */
    public void executeInitCommandSystemStatus() {
        ActiveUser activeUser = LoginHandler.getActiveUserByUsername(request.context.username);
        if (activeUser == null) {
            Parser.addResponse(request.getKey(), Parser.encodeArgument("response", Parameters.ErrorActiveUserNotFound));
            return;
        }

        Parser.addResponse(request.getKey(), Parser.encodeArgumentList(activeUser.getSystemStatusAsArguments()));
    }

    /**
     * executes init command for requesting system spec
     */
    public void executeInitCommandSystemSpec() {
        // get the system specs
        SystemSpec spec =  SystemSpec.getUserSystemSpecs(request.context.username);
        // sanity checks
        if (spec == null) {
            Parser.addResponse(request.getKey(), Parser.encodeArgument("response", Parameters.ErrorSystemSpecsNotFound));
            return;
        }

        // more sanity checks
        String error = null;
        if (spec.motherboard == null)
            error = Parameters.ErrorSystemSpecMotherboardNotFound;
        else if (spec.cpus == null)
            error = Parameters.ErrorSystemSpecCpuNotFound;
        else if (spec.rams == null)
            error = Parameters.ErrorSystemSpecRamNotFound;
        else if (spec.hdds == null)
            error = Parameters.ErrorSystemSpecHddNotFound;
        else if (spec.networkCard == null)
            error = Parameters.ErrorSystemSpecNetworkCardNotFound;

        // return if there is an error
        if (error != null) {
            Parser.addResponse(request.getKey(), Parser.encodeArgument("response", error));
            return;
        }

        // parse response
        Parser.addResponse(request.getKey(), Parser.encodeArgumentList(spec.getSpecAsArguments()));
    }

    /**
     * executes init command for requesting account balance
     */
    public void executeInitCommandAccountBalance() {
        // get all the accounts
        List<Account> accounts = DomainsManager.getBankAccountsByUsername(request.context.username);

        // parse the account list
        String response = "";
        for (Account a :
                accounts) {
            response += a.bank.name + ":" + a.balance + ",";
        }

        // delete the last ',' if needed
        if (response.endsWith(","))
            response = response.substring(0, response.length() - 1);

        Parser.addResponse(request.getKey(), Parser.encodeArgument("response", response));
    }

    /**
     * executes init command for requesting auto complete list
     */
    public void executeInitCommandAutoCompleteList() {
        // get auto complete
        HashMap<String, Command> result = getAutocompleteCommands(request.context);

        // parse the auto complete list
        String response = "";
        for (String c :
                result.keySet())
            response += c + ",";

        // delete the last ',' if needed
        if (response.endsWith(","))
            response = response.substring(0, response.length() - 1);

        Parser.addResponse(request.getKey(), Parser.encodeArgument("response", response));
    }

    /**
     * gets all the available commands for the current {@link CommandRequest}
     * @return all the available commands
     */
    public HashMap<String, Command> getAccessibleCommands() {
        return getAccessibleCommands(request.context);
    }

    /**
     * gets all the available commands for the supplied {@link CommandContext}
     * @param context the {@link CommandContext} to check with
     * @return all the accessible commands
     */
    public static HashMap<String, Command> getAccessibleCommands(CommandContext context) {
        HashMap<String, Command> accessibleCommands = new HashMap<>(CommandManager.commandList);

        ActiveUser user = LoginHandler.getActiveUserByUsername(context.username);
        if (user == null)
            return null;
        HashMap<String, Command> allCommands = user.getAvailableCommands();
        for (Command c :
                allCommands.values()) {
            if (c.access == CommandAccess.System)
                accessibleCommands.put(c.name, c);
        }

        return accessibleCommands;
    }

    /**
     * gets all the available player scripts for the supplied {@link CommandContext}
     * @param context the {@link CommandContext} to check with
     * @return all the accessible commands
     */
    public static HashMap<String, Command> getAccessiblePlayerScripts(CommandContext context) {
        HashMap<String, Command> accessibleScripts = new HashMap<>();

        ActiveUser user = LoginHandler.getActiveUserByUsername(context.username);
        if (user == null)
            return null;
        HashMap<String, Command> allCommands = user.getAvailableCommands();
        for (Command c :
                allCommands.values()) {
            if (c.access != CommandAccess.System)
                accessibleScripts.put(c.name, c);
        }

        return accessibleScripts;
    }

    /**
     * gets all the available commands and player scripts for the supplied {@link CommandContext}
     * @param context the {@link CommandContext} to check with
     * @return all the accessible commands and player scripts
     */
    public static HashMap<String, Command> getAllAccessibleCommands(CommandContext context) {
        if (context == null)
            return null;

        ActiveUser user = LoginHandler.getActiveUserByUsername(context.username);
        if (user == null)
            return null;

        return user.getAvailableCommands();
    }

    /**
     * gets only the commands available to auto complete
     * @param context the context in which to check
     * @return the auto completable commands
     */
    public HashMap<String, Command> getAutocompleteCommands(CommandContext context) {
        if (context == null)
            return null;

        ActiveUser user = LoginHandler.getActiveUserByUsername(context.username);
        if (user == null)
            return null;

        return user.getAutoCompleteCommands();
    }

    /**
     * gets the command needed to run
     * @return the command to actually run
     */
    public Command parseCommand() {
        HashMap<String, Command> commandsToSearch = getAllAccessibleCommands(request.context);
        return parseCommand(commandsToSearch);
    }

    /**
     * searches for a command from a given starting hash map of commands
     * @param startingPoint the starting hash map for the search
     * @return the command to actually run
     */
    public Command parseCommand(HashMap<String, Command> startingPoint) {
        // TODO : fix
        Logger.log("Worker.workerStart", "commands length is " + commands.size());

        HashMap<String, Command> commandsToSearch = startingPoint;
        Command commandFound = null;
        for (String c :
                commands) {
            Logger.log("Worker.parseCommand", "searching for " + c + " in " + commandsToSearch.size() + " commands");
            // command does not exists in collection
            if (!commandsToSearch.containsKey(c)) {
                error = Parameters.ErrorCommandDoesNotExistsPrefix + c;
                return null;
            }

            commandFound = commandsToSearch.get(c);
            commandsToSearch = commandFound.subCommands;
        }

        if (commandFound == null) {
            error = Parameters.ErrorCommandDoesNotExists;
            return null;
        }

        return commandFound;
    }

    /**
     * checks the command syntax and populates {@code commands} and {@code argument}
     * @param input the input to check
     * @return whether there were errors, if true, check {@code error} field for description
     */
    public boolean checkSyntax(String input) {
        // check for '{' and '}'
        if (input.contains("{") != input.contains("}")) {
            error = Parameters.SyntaxErrorArgumentsParenthesisMismatch;
            return false;
        }

        int commandEnd = input.length();

        // check argument list syntax
        if (input.contains("{")) {
            int startIndex = input.indexOf('{');
            int endIndex = input.indexOf('}');

            commandEnd = startIndex;

            // get only the argument list
            String args = input.substring(startIndex + 1, endIndex);
            String[] argsArray = args.split(",");
            for (String arg :
                    argsArray) {
                // check for ':' between name and value
                String[] argParts = arg.split(":");
                if (argParts.length != 2) {
                    error = Parameters.SyntaxErrorInvalidArgumentSyntax;
                    return false;
                }

                if (arg.equals(Parameters.InitCommandTemplate)) {
                    initCommand = true;
                    continue;
                }

                // add argument to list
                String type;
                if (argParts[1].contains("\""))
                    type = "String";
                else if (argParts[1].contains("."))
                    type = "float";
                else
                    type = "int";
                arguments.add(new Argument(argParts[0], type, argParts[1]));
            }
        }

        // add commands
        String command = input.substring(0, commandEnd);
        if (command.contains(".")) {
            String[] commandSplat = command.split(".");
            Collections.addAll(commands, commandSplat);
        }
        else
            commands.add(command);

        return true;
    }
}
