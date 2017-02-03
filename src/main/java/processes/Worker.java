package processes;

import Commands.Help;
import database_objects.CommandsTableRow;
import objects.*;
import interface_objects.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Worker {
    // region static variables
    public static CommandRequest requestToHandle;
    public static ThreadedJobFactory threadFactory = new ThreadedJobFactory(Parameters.maxWorkerThreads);
    public static ConcurrentHashMap<String, Command> commandList = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Command> allCommands = new ConcurrentHashMap<>();
    // endregion

    // region public variables
    public CommandRequest request;
    public String error;
    public List<String> commands;
    public List<Argument> arguments;
    // endregion

    // static initializer (all system commands)
    static {
        Command help = new Command(0, "help", new Help(), CommandAccess.System);
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
            if (error != "")
                Parser.addResponse(request.getKey(), error);

            return;
        }

        Command commandToRun = parseCommand();
        if (commandToRun != null) {
            // run command and add response
            String response = commandToRun.entry.execute(request.context, commandToRun.name);

            Parser.addResponse(request.getKey(), response);
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
     * gets all the available commands for the supplied {@link CommandRequest}
     * @param cr the {@link CommandRequest} to check with
     * @return all the available commands
     */
    public static HashMap<String, Command> getAccessibleCommands(CommandContext cr) {
        HashMap<String, Command> accessibleCommands = new HashMap<>(commandList);

        // TODO : add all the other accessible commands (player scripts from db, etc.) using request.context

        return accessibleCommands;
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
                // add argument to list
                String type;
                if (argParts[1].contains("'"))
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
