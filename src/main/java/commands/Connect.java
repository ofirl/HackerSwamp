package commands;

import managers.CommandManager;
import managers.DomainsManager;
import objects.*;

import java.util.HashMap;

/**
 * accepted arguments for the connect command :
 * filter : "commands"
 */
public class Connect extends BaseCommand{

    public static Command superCommand;
    public static HashMap<String, Argument> acceptedArguments = new HashMap<>();

    static {
        superCommand = CommandManager.allCommands.get(Parameters.CommandNameConnect);
        acceptedArguments.put("arg1", new Argument("arg1", String.class));
        acceptedArguments.put("domain", new Argument("domain", String.class));
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
        Argument domain = args.get("arg1");
        if (domain == null)
            domain = args.get("domain");

        if (domain != null)
            return DomainsManager.connectToDomain(domain.value, context, args);

        return getSubCommands(superCommand);
    }
}
