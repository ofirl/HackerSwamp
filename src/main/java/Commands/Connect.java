package Commands;

import objects.*;

public class Connect extends BaseCommand{

    /**
     * accepted arguments for the connect command :
     * filter : "commands"
     */
    static {
        //acceptedArguments.add(new Argument("filter", String.class));
    }

    /**
     * empty constructor for the
     */
    public Connect() {
        this(null);
    }

    /**
     * constructor
     * @param context the context to run in
     */
    public Connect(CommandContext context) {
        super(context);
        mainName = Parameters.CommandNameConnect;
        superCommand = CommandManager.allCommands.get(mainName);
    }

    /**
     * implementation of the abstract method
     * @param context the context of the new instance
     * @return a new instance with the given context
     */
    public Connect createInstance(CommandContext context) {
        return new Connect(context);
    }

    /**
     * help command
     * @return general help
     */
    public String main() {
        // TODO : implement
        return getSubCommands();
    }
}
