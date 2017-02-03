import database_objects.CommandsTableRow;
import objects.*;
import interface_objects.*;

import java.util.*;

public class Worker {
    // static variables
    public static CommandRequest requestToHandle;
    public static ThreadedJobFactory threadFactory = new ThreadedJobFactory(Parameters.maxWorkerThreads);
    public static HashMap<String, Command> commandList = new HashMap<>();
    public static HashMap<String, Command> allCommands = new HashMap<>();

    // public variables
    public CommandRequest request;

    // static initializer (all system commands)
    static {

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
            Command command = new Command(c.id, c.name);

            String[] argsArray = c.arguments.split(",");
            List<Argument> argsList = new ArrayList<>();
            for (String a :
                    argsArray) {
                String[] aParts = a.split(":");
                argsList.add(new Argument(aParts[0], aParts[1]));
            }
            command.arguments = argsList;

            // add to commandList
            if (!commandList.containsKey(c.owner))
                commandList.put(c.owner, new Command(0, c.owner));

            commandList.get(c.owner).subCommands.put(c.name, command);

            allCommands.put(c.name, command);
        }
    }

    /**
     * gets a command by id
     * @param id id to search
     * @return a command
     */
    public static Command getCommandById(int id) {
        if (id == 0)
            return null;

        for (Command c :
                allCommands.values()) {
            if (c.id == id)
                return c;
        }

        return null;
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
        // TODO : work on worker logic



        Parser.addResponse(request.getKey(), request.getKey());
    }
}
