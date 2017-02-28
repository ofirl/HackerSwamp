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

    // endregion

    // region public variables
    public CommandRequest request;
    public String error;
    public List<String> commands = new ArrayList<>();
    public List<Argument> arguments = new ArrayList<>();
    public String initCommand = "";
    // endregion

    /**
     * entry point for worker process
     * @param args main program arguments
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        // init the entire syscmd
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
     * main init method for the entire syscmd
     */
    public static void initSystem() {
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
        Logger.log("Worker.workerStart", "got " + request.command);

        // sanity check
        if (request.command == null || request.command.equals("")) {
            Parser.addResponse(request.getKey(), Parameters.ErrorNullCommand);
            return;
        }

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

        // parse the input and populate commands and arguments
        if (!checkSyntax(request.command)) {
            if (error.equals(""))
                error = Parameters.ErrorUnknownError;

            Parser.addResponse(request.getKey(), error);
            return;
        }

        if (commands.size() == 1 && commands.get(0).equals("init")) {
            if (arguments.size() == 1) {
                initCommand = arguments.get(0).value;

                if (initCommand.equals("")) {
                    Parser.addResponse(request.getKey(), Parameters.ErrorInvalidInitCommand);
                    return;
                }

                boolean foundInitCommand = true;

                if (initCommand.equals(Parameters.InitCommandAutoCompleteList))
                    executeInitCommandAutoCompleteList();
                else if (initCommand.equals(Parameters.InitCommandAccountBalance))
                    executeInitCommandAccountBalance();
                else if (initCommand.equals(Parameters.InitCommandMacros))
                    executeInitCommandMacros();
                else if (initCommand.equals(Parameters.InitCommandSystemSpec))
                    executeInitCommandSystemSpec();
                else if (initCommand.equals(Parameters.InitCommandSystemStatus))
                    executeInitCommandSystemStatus();
                else
                    foundInitCommand = false;

                if (foundInitCommand)
                    return;
            }
        }

        String response = null;
        Command commandToRun;

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
     * executes init command for requesting syscmd status
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
     * executes init command for requesting syscmd spec
     */
    public void executeInitCommandSystemSpec() {
        // get the syscmd specs
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
        Logger.log("Worker.parseCommand", "commands length is " + commands.size());

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
     * @return whether the check succeeded, if false (errors), check {@code error} field for description
     */
    public boolean checkSyntax(String input) {
        commands = new ArrayList<>();
        arguments = new ArrayList<>();

        // check for '{' and '}'
        if (input.contains("{") != input.contains("}")) {
            error = Parameters.SyntaxErrorArgumentsParenthesisMismatch;
            return false;
        }

        String commandToParse = null;

        int commandEnd = input.length();

        // check argument list syntax
        // { } format
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

                // check argument type
                String type = checkArgumentType(argParts[1]);

                // add argument to list
                arguments.add(new Argument(argParts[0].trim(), type, argParts[1].trim()));
            }

            commandToParse = input.substring(0, commandEnd);
        }
        else { // "command arg1" format
            String[] commandParts = input.split(" ");
            if (commandParts.length == 0)
                return false;

            commandToParse = commandParts[0];

            for (int i = 1; i < commandParts.length; i++)
                arguments.add(new Argument("arg" + i, checkArgumentType(commandParts[i]), commandParts[i]));
        }

        if (commandToParse.contains(".")) {
            String[] commandSplat = commandToParse.split("\\.");
            commandSplat[0] = commandSplat[0].trim();
            commandSplat[commandSplat.length - 1] = commandSplat[commandSplat.length - 1].trim();
            for (String s :
                    commandSplat)
                commands.add(s);
        }
        else
            commands.add(commandToParse.trim());

        return true;
    }

    /**
     * checks the argument type
     * @param arg the argument to check
     * @return the argument type name (int, float or string)
     */
    public String checkArgumentType(String arg) {
        String type;
        try {
            //noinspection ResultOfMethodCallIgnored
            Integer.parseInt(arg);
            type = "int";
        }
        catch (Exception e) {
            try {
                //noinspection ResultOfMethodCallIgnored
                Double.parseDouble(arg);
                type = "double";
            }
            catch (Exception ex) {
                type = "java.lang.String";
            }
        }

        return type;
    }
}
