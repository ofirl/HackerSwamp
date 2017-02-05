package Commands;

import objects.*;
import processes.Worker;

import java.util.HashMap;
import java.util.List;

public class Help extends BaseCommand {

    /**
     * accepted arguments for the help command :
     * filter : "commands"
     */
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
        superCommand = Worker.allCommands.get(mainName);
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
            if (filter.value.equals("commands"))
                commands = Worker.getAccessibleCommands(context);
            else if (filter.value.equals("scripts"))
                commands = Worker.getAccessiblePlayerScripts(context);
            else
                commands = Worker.getAllAccessibleCommands(context);
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
