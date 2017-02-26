package commands;

import managers.CommandManager;
import managers.Logger;
import objects.*;
import processes.Worker;

import java.util.*;

/**
 * accepted arguments for the help command :
 * filter : "commands"
 */
public class Help extends BaseCommand {

    public static Command superCommand;
    public static HashMap<String, Argument> acceptedArguments = new HashMap<>();

    static {
        acceptedArguments.put("filter", new Argument("filter", String.class));
        acceptedArguments.put("security", new Argument("security", String.class));
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
        superCommand = CommandManager.allCommands.get(mainName);
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
     * calling the appropriate function, defined by {@code subCommand}, checking the received arguments first
     * @param subCommand the function to call
     * @return the command result
     */
    @Override
    public String call(String subCommand) {
        // check for invalid argument
        for (String arg :
                args.keySet()) {
            if (!acceptedArguments.containsKey(arg) || acceptedArguments.get(arg).type != args.get(arg).type)
                return Parameters.ErrorCommandInvalidArguments;
        }

        // call the right method (sub command)
        return super.call(subCommand);
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
                if (!commands.get(com).securityRating.equals(security.value))
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
