package commands;

import interface_objects.LoginHandler;
import managers.CommandManager;
import managers.DomainsManager;
import objects.*;
import processes.WebListener;

import java.util.ArrayList;
import java.util.List;

public class System extends BaseCommand{

    public static Command superCommand;
    public static List<Argument> acceptedArguments = new ArrayList<>();

    static {
        superCommand = CommandManager.allCommands.get(Parameters.CommandNameSystem);
        //acceptedArguments.add(new Argument("filter", String.class));
    }

    /**
     * empty constructor
     */
    public System() {
        this(null);
    }

    /**
     * constructor
     * @param context the context to run in
     */
    public System(CommandContext context) {
        super(context, Parameters.CommandNameSystem);
    }

    /**
     * implementation of the abstract method
     * @param context the context of the new instance
     * @return a new instance with the given context
     */
    public System createInstance(CommandContext context) {
        return new System(context);
    }

    /**
     * system command
     * @return general help
     */
    public String main() {
        // testing purposes
        //WebListener.executePost("https://" + LoginHandler.getActiveUserByUsername(context.username).clientIp + ":2525", "test");

        Argument domain = args.get("domain");
        if (domain != null)
            return DomainsManager.connectToDomain(domain.value, context, args);

        return getSubCommands(superCommand);
    }

    /**
     * system.spec command
     * @return detailed print of the system spec
     */
    public String spec() {
        ActiveUser activeUser = LoginHandler.getActiveUserByUsername(context.username);
        if (activeUser == null)
            return Parameters.ErrorActiveUserNotFound;

        SystemSpec systemSpec = activeUser.getSystemSpec();
        if (systemSpec == null)
            return Parameters.ErrorSystemSpecsNotFound;

        return systemSpec.getDetailedPrint();
    }
}
