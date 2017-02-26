package commands;

import managers.CommandManager;
import managers.DomainsManager;
import objects.*;

import java.util.HashMap;

public class Connect extends BaseCommand{

    public static Command superCommand;
    public static HashMap<String, HashMap<String, Argument>> acceptedArguments = new HashMap<>();

    static {
        // super command
        superCommand = CommandManager.allCommands.get(Parameters.CommandNameConnect);

        // sub commands hash maps init
        acceptedArguments.put("connect", new HashMap<>());

        // connect
        acceptedArguments.get("connect").put("arg1", new Argument("arg1", String.class));
        acceptedArguments.get("connect").put("domain", new Argument("domain", String.class));
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
     * connect command
     * @return connect to a domain
     */
    public String main() {
        // check for invalid argument
        HashMap<String, Argument> acceptedCommandArgs = acceptedArguments.get("connect");
        for (String arg :
                args.keySet()) {
            if (!acceptedCommandArgs.containsKey(arg) || acceptedCommandArgs.get(arg).type != args.get(arg).type)
                return Parameters.ErrorCommandInvalidArguments;
        }

        Argument domain = args.get("arg1");
        if (domain == null)
            domain = args.get("domain");

        if (domain != null)
            return DomainsManager.connectToDomain(domain.value, context, args);

        return getSubCommands(superCommand);
    }
}
