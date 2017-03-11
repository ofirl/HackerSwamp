package commands;

import interface_objects.LoginHandler;
import managers.CommandManager;
import managers.DomainsManager;
import objects.*;

import java.util.HashMap;

public class System extends BaseCommand{

    public static Command superCommand;
    public static HashMap<String, HashMap<String, Argument>> acceptedArguments = new HashMap<>();

    static {
        // super command
        superCommand = CommandManager.getCommandByName(Parameters.CommandNameSystem);

        // sub commands hash maps init
        acceptedArguments.put("system", new HashMap<>());
        acceptedArguments.put("spec", new HashMap<>());

        // syscmd
        //acceptedArguments.get("syscmd").put("filer", new Argument("filter", String.class));

        // syscmd
        //acceptedArguments.get("spec").put("filer", new Argument("filter", String.class));
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
     * syscmd command
     * @return general help
     */
    public String main() {
        // check for invalid argument
        if (!checkArguments(acceptedArguments.get("system")))
            return Parameters.ErrorCommandInvalidArguments;

        // testing purposes
        //WebListener.executePost("http://" + LoginHandler.getActiveUserByUsername(context.username).clientIp + ":7777", "test");

        return getSubCommands(superCommand);
    }

    /**
     * syscmd.spec command
     * @return detailed print of the syscmd spec
     */
    public String spec() {
        // check for invalid argument
        if (!checkArguments(acceptedArguments.get("spec")))
            return Parameters.ErrorCommandInvalidArguments;

        ActiveUser activeUser = LoginHandler.getActiveUserByUsername(context.username);
        if (activeUser == null)
            return Parameters.ErrorActiveUserNotFound;

        SystemSpec systemSpec = activeUser.getSystemSpec();
        if (systemSpec == null)
            return Parameters.ErrorSystemSpecsNotFound;

        return systemSpec.getDetailedPrint();
    }
}
