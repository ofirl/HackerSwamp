package processes;

import Commands.BaseCommand;
import Commands.Help;
import Domains.Bank;
import Domains.BaseDomain;
import database_objects.AutocompleteTableRow;
import database_objects.CommandsTableRow;
import database_objects.PlayersTableRow;
import objects.*;
import interface_objects.*;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Worker {
    // region static variables
    public static CommandRequest requestToHandle;
    public static ThreadedJobFactory threadFactory = new ThreadedJobFactory(Parameters.maxWorkerThreads);
    public static ConcurrentHashMap<String, Command> commandList = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Command> allCommands = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, BaseDomain> allDomains = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Bank> bankDomains = new ConcurrentHashMap<>();
    // TODO : add domain manager and command manager, worker is not responsible for them
    // endregion

    // region public variables
    public CommandRequest request;
    public String error;
    public List<String> commands;
    public List<Argument> arguments;
    public boolean initCommand = false;
    // endregion

    // system commands initializer
    static {
        // help
        addSystemCommand(Parameters.CommandNameHelp, new Help(), true);
        // help.commands
        addSystemCommand(Parameters.CommandNameHelpCommands, new Help(), false);
        // TODO : add implementation for the commands
    }

    // domains initializer
    static {

    }

    /**
     * created and adds a system command to the lists
     * @param name the name of hte ocmmamd
     * @param baseCommand the class that implements the command
     */
    public static void addSystemCommand(String name, BaseCommand baseCommand, boolean mainCommand) {
        Command cmd = new Command(0, name, baseCommand, CommandAccess.System);
        allCommands.put(cmd.name, cmd);
        if (mainCommand)
            commandList.put(cmd.name, cmd);
    }

    /**
     * entry point for worker process
     * @param args
     */
    public static void main(String[] args) {
        initializeCommands();

        while (true) {
            requestToHandle = null;
            // make sure we have a requestToHandle
            while (requestToHandle == null)
                requestToHandle = Parser.receiveCommand();

            // create a thread and handle the requestToHandle
            threadFactory.newThread(new Worker(requestToHandle)::workerStart, null, "workerThread");
        }
    }

    /**
     * initializing command lists from database
     */
    public static void initializeCommands() {
        // get command list from db
        List<CommandsTableRow> dbCommands = DatabaseHandler.getTableElements(DatabaseTables.Commands);
        // add command to commandList (if needed) and allCommands
        for (CommandsTableRow c :
                dbCommands) {
            // TODO : get the correct callable instead of null
            Command command = new Command(c.id, c.name, null, CommandAccess.valueOf(c.access));

            String[] argsArray = c.arguments.split(",");
            List<Argument> argsList = new ArrayList<>();
            for (String a :
                    argsArray) {
                String[] aParts = a.split(":");
                argsList.add(new Argument(aParts[0], aParts[1]));
            }
            command.arguments = argsList;

            // add to commandList and allCommands
            if (!commandList.containsKey(c.owner)) {
                // TODO : get the correct callable instead of null
                commandList.put(c.owner, new Command(0, c.owner, null, CommandAccess.valueOf(c.access)));
                allCommands.put(c.owner, commandList.get(c.owner));
            }

            commandList.get(c.owner).subCommands.put(c.name, command);

            allCommands.put(c.name, command);
        }
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
        if (!checkSyntax(request.command)) {
            if (error.equals(""))
                Parser.addResponse(request.getKey(), error);

            return;
        }

        if (initCommand) {
            HashMap<String, Argument> argsMap = new HashMap<>();
            arguments.forEach((a) -> argsMap.put(a.name, a));
            // asked for auto complete list
            if (argsMap.get(Parameters.InitCommandAutoCompleteList) != null) {
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
            // asked for system spec
            else if (argsMap.get(Parameters.InitCommandSystemSpec) != null) {
                // TODO : write the init code
            }
            // asked for account balance
            else if (argsMap.get(Parameters.InitCommandAccountBalance) != null) {
                // TODO : write the init code
            }
            // asked for system status
            else if (argsMap.get(Parameters.InitCommandSystemStatus) != null) {
                // TODO : write the init code
            }
            // asked for macros
            else if (argsMap.get(Parameters.InitCommandMacros) != null) {
                // TODO : write the init code
            }
        }

        Command commandToRun = parseCommand();
        if (commandToRun != null) {
            // run command and add response
            String response = commandToRun.execute(request.context, commandToRun.name, arguments);

            Parser.addResponse(request.getKey(), Parser.encodeArgument("response", response));
            return;
        }

        // TODO : search also in the domains and location specific commands

        //Parser.addResponse(request.getKey(), request.getKey());
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
        HashMap<String, Command> accessibleCommands = new HashMap<>(commandList);

        // TODO : filter accessible commands only (based on context)

        return accessibleCommands;
    }

    /**
     * gets all the available player scripts for the supplied {@link CommandContext}
     * @param context the {@link CommandContext} to check with
     * @return all the accessible commands
     */
    public static HashMap<String, Command> getAccessiblePlayerScripts(CommandContext context) {
        HashMap<String, Command> accessibleScripts = new HashMap<>();

        // TODO : add all the other accessible commands (player scripts from db, etc.) using context

        return accessibleScripts;
    }

    /**
     * gets all the available commands and player scripts for the supplied {@link CommandContext}
     * @param context the {@link CommandContext} to check with
     * @return all the accessible commands and player scripts
     */
    public static HashMap<String, Command> getAllAccessibleCommands(CommandContext context) {
        HashMap<String, Command> accessibleCommands = getAccessibleCommands(context);
        accessibleCommands.putAll(getAccessiblePlayerScripts(context));

        // TODO : add all the other accessible commands (player scripts from db, etc.) using context

        return accessibleCommands;
    }

    /**
     * gets only the commands available to auto complete
     * @param context the context in which to check
     * @return the auto completable commands
     */
    public HashMap<String, Command> getAutocompleteCommands(CommandContext context) {
        // TODO : add database view for selecting the excludes with their names and change accordingly
        // get all accessible
        HashMap<String, Command> accessible = getAllAccessibleCommands(context);

        // get excludes
        String filter = "username='" + context.username + "' AND " + "action='exclude'";
        List<AutocompleteTableRow> excludes = DatabaseHandler.getTableElements(DatabaseTables.Autocomplete, null, filter);

        if (excludes != null) {
            // remove excludes from accessible list
            for (AutocompleteTableRow a :
                    excludes) {
                accessible.remove("see to do");
            }
        }

        return accessible;
    }

    /**
     * gets the command needed to run
     * @return the command to actually run
     */
    public Command parseCommand() {
        HashMap<String, Command> commandsToSearch = getAccessibleCommands();
        Command commandFound = null;
        for (String c :
                commandsToSearch.keySet()) {
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

                if (arg.equals(Parameters.InitCommandTemplate))
                    initCommand = true;

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
        String[] commandSplat = command.split(".");
        Collections.addAll(commands, commandSplat);

        return true;
    }
}
