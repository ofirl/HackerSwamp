package Commands;

import objects.*;
import processes.Worker;

import java.util.HashMap;

public class Help extends BaseCommand {


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
        mainName = "help";
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
        HashMap<String, Command> commands = Worker.getAccessibleCommands(context);

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
