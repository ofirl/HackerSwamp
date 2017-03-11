package commands;

import managers.CommandManager;
import managers.Logger;
import objects.*;
import processes.Worker;

import java.util.*;

public class Help extends BaseCommand {

    public static Command superCommand;
    public static HashMap<String, HashMap<String, Argument>> acceptedArguments = new HashMap<>();

    static {
        // super command
        superCommand = CommandManager.getCommandByName(Parameters.CommandNameHelp);

        // sub commands hash maps init
        acceptedArguments.put("help", new HashMap<>());
        acceptedArguments.put("commands", new HashMap<>());

        // commands
        acceptedArguments.get("commands").put("filter", new Argument("filter", String.class));
        acceptedArguments.get("commands").put("security", new Argument("security", String.class));
    }

    /**
     * empty constructor for the
     */
    public Help() {
        this(null);
    }

    /**
     * constructor
     * @param context the context to run in
     */
    public Help(CommandContext context) {
        super(context, Parameters.CommandNameHelp);
    }

    /**
     * implementation of the abstract method
     * @param context the context of the new instance
     * @return a new instance with the given context
     */
    public Help createInstance(CommandContext context) {
        return new Help(context);
    }

    /**
     * help command
     * @return general help
     */
    public String main() {
        return getSubCommands(superCommand);
    }

    /**
     * help.commands command
     * @return list of commands
     */
    public String commands() {
        // check for invalid argument
        if (!checkArguments(acceptedArguments.get("commands")))
            return Parameters.ErrorCommandInvalidArguments;

        String output = "";
        HashMap<String, Command> commands = null;
        Argument filter = args.get("filter");
        if (filter != null) {
            switch (filter.value) {
                case "commands":
                    commands = Worker.getAccessibleCommands(context);
                    break;
                case "scripts":
                    commands = Worker.getAccessiblePlayerScripts(context);
                    break;
                case "all" :
                    commands = Worker.getAllAccessibleCommands(context);
                    break;
                default:
                    output += "available \"filter\" values are : commands, scripts and all\n\n";
                    break;
            }
        }
        else {
            output += "a filter argument is required\n";
            output += "available \"filter\" values are : commands, scripts and all\n\n";
            output += "a security argument is optional and will filter by security rating\n";
            return output;
        }

        if (commands == null) {
            String msg = "Error retrieving commands for : filter = " + filter.value + ", username = " + context.username;
            Logger.log("Help.commands", msg);
            return Parameters.ErrorUnknownError;
        }

        // filter commands by security
        Argument security = args.get("security");
        if (security != null) {
            HashMap<String, Command> filteredCommands = new HashMap<>();
            for (String com :
                    commands.keySet()) {
                if (commands.get(com).securityRating.compareTo(CommandSecurityRating.valueOf(security.value)) <= 0)
                    filteredCommands.put(com, commands.get(com));
            }
            commands = filteredCommands;
        }

        output += "";
        for (String sub :
                commands.keySet()) {
            Command parent = commands.get(sub).parent;
            output += parent == null ? "" : parent.name + ".";
            output += sub + "\n";
        }

        return output;
    }
}
