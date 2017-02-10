package commands;

import objects.*;
import processes.Worker;

import java.util.*;

/**
 * accepted arguments for the help command :
 * filter : "commands"
 */
public class Help extends BaseCommand {

    static {
        acceptedArguments.add(new Argument("filter", String.class));
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
        super(context);
        mainName = Parameters.CommandNameHelp;
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
     * help command
     * @return general help
     */
    public String main() {
        return getSubCommands();
    }

    /**
     * help.commands command
     * @return list of commands
     */
    public String commands() {
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
                default:
                    commands = Worker.getAllAccessibleCommands(context);
                    break;
            }
        }

        if (commands == null)
            return "";

        String output = "";
        for (String sub :
                commands.keySet()) {
            Command parent = commands.get(sub).parent;
            output += parent == null ? "" : parent.name + ".";
            output += sub + "\n";
        }

        return output;
    }
}
