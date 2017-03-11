package commands;

import database_objects.LogsTableRow;
import interface_objects.DatabaseHandler;
import interface_objects.DatabaseTables;
import managers.CommandManager;
import managers.DomainsManager;
import objects.*;

import java.util.HashMap;
import java.util.List;

public class Logs extends BaseCommand {
    public static Command superCommand;
    public static HashMap<String, HashMap<String, Argument>> acceptedArguments = new HashMap<>();

    static {
        // super command
        superCommand = CommandManager.getCommandByName(Parameters.CommandNameLogs);

        // sub commands hash maps init
        acceptedArguments.put("logs", new HashMap<>());
        acceptedArguments.put("view", new HashMap<>());
        acceptedArguments.put("delete", new HashMap<>());

        // view
        acceptedArguments.get("view").put("arg1", new Argument("arg1", int.class));
        acceptedArguments.get("view").put("count", new Argument("count", int.class));

        // delete
        acceptedArguments.get("delete").put("arg1", new Argument("arg1", String.class));
        acceptedArguments.get("delete").put("count", new Argument("count", int.class));
    }

    /**
     * empty constructor for the
     */
    public Logs() {
        this(null);
    }

    /**
     * constructor
     * @param context the context to run in
     */
    public Logs(CommandContext context) {
        super(context, Parameters.CommandNameLogs);
    }

    /**
     * implementation of the abstract method
     * @param context the context of the new instance
     * @return a new instance with the given context
     */
    public Logs createInstance(CommandContext context) {
        return new Logs(context);
    }

    /**
     * logs command
     * @return general help
     */
    public String main() {
        // TODO : return general help
        return getSubCommands(superCommand);
    }

    /**
     * logs.view command
     * @return list of log items
     */
    public String view() {
        // check for invalid argument
        if (!checkArguments(acceptedArguments.get("view")))
            return Parameters.ErrorCommandInvalidArguments;

        String output = "";

        // TODO : implement!
        Object[] funcArgs = null;

        int count = args.containsKey("arg1") ? args.get("arg1").castValue(Integer.class) : 0;
        if (count == 0)
            count = args.containsKey("count") ? args.get("count").castValue(Integer.class) : 0;

        if (count < 1)
            return Parameters.CommandUsageLogsView;

        if (count == 0)
            funcArgs = null;
        else
            funcArgs = new Object[] { count };

        String filter = "domain=" + DomainsManager.getDomainByName(context.location).id;

        List<LogsTableRow> logs = DatabaseHandler.callFunction(DatabaseTables.Recent_Logs, funcArgs, null, filter);
        if (logs == null)
            return Parameters.ErrorLogsNotFound;
        if (logs.size() == 0)
            return "Logs are empty...";

        for (LogsTableRow l :
                logs)
            output += l.time + " : " + l.entry + "\n";

        return output;
    }

    /**
     * logs.delete command
     * @return a response
     */
    public String delete() {
        // check for invalid argument
        if (!checkArguments(acceptedArguments.get("delete")))
            return Parameters.ErrorCommandInvalidArguments;

        String output = "";

        // TODO : implement!
        output += "implement logs.delete";

        return output;
    }
}
