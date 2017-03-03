package commands;

import interface_objects.LoginHandler;
import items.BaseItem;
import managers.CommandManager;
import managers.ItemManager;
import managers.Logger;
import objects.*;

import java.util.HashMap;

public class Logs extends BaseCommand {
    public static Command superCommand;
    public static HashMap<String, HashMap<String, Argument>> acceptedArguments = new HashMap<>();

    static {
        // super command
        superCommand = CommandManager.allCommands.get(Parameters.CommandNameLogs);

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
        output += "implement logs.view";

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
