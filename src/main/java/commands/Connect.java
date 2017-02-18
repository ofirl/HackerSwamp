package commands;

import managers.CommandManager;
import managers.DomainsManager;
import objects.*;

import java.util.ArrayList;
import java.util.List;

/**
 * accepted arguments for the connect command :
 * filter : "commands"
 */
public class Connect extends BaseCommand{

    public static Command superCommand;
    public static List<Argument> acceptedArguments = new ArrayList<>();

    // TODO : check accepted arguments somewhere?
    static {
        superCommand = CommandManager.allCommands.get(Parameters.CommandNameConnect);
        acceptedArguments.add(new Argument("arg1", String.class));
    }

    /**
     * empty constructor
     */
    public Connect() {
        this(null);
    }

    /**
     * constructor
     * @param context the context to run in
     */
    public Connect(CommandContext context) {
        super(context, Parameters.CommandNameConnect);
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
        Argument domain = args.get("arg1");
        if (domain != null)
            return DomainsManager.connectToDomain(domain.value, context, args);

        return getSubCommands(superCommand);
    }
}
