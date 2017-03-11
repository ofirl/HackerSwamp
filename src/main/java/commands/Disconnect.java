package commands;

import interface_objects.LoginHandler;
import managers.CommandManager;
import managers.DomainsManager;
import objects.*;

import java.util.HashMap;

public class Disconnect extends BaseCommand {
    public static Command superCommand;
    public static HashMap<String, HashMap<String, Argument>> acceptedArguments = new HashMap<>();

    static {
        // super command
        superCommand = CommandManager.getCommandByName(Parameters.CommandNameDisconnect);

        // sub commands hash maps init
        acceptedArguments.put("disconnect", new HashMap<>());

        // disconnect
        //acceptedArguments.get("disconnect").put("arg1", new Argument("arg1", String.class));
    }

    /**
     * empty constructor
     */
    public Disconnect() {
        this(null);
    }

    /**
     * constructor
     * @param context the context to run in
     */
    public Disconnect(CommandContext context) {
        super(context, Parameters.CommandNameDisconnect);
    }

    /**
     * implementation of the abstract method
     * @param context the context of the new instance
     * @return a new instance with the given context
     */
    public Disconnect createInstance(CommandContext context) {
        return new Disconnect(context);
    }

    /**
     * disconnect command
     * @return disconnects from a domain
     */
    public String main() {
        // check for invalid argument
        if (!checkArguments(acceptedArguments.get("disconnect")))
            return Parameters.ErrorCommandInvalidArguments;

        ActiveUser user = LoginHandler.getActiveUserByUsername(context.username);
        if (user == null)
            return Parameters.ErrorActiveUserNotFound;

        if (user.getLocation().equals("localhost"))
            return Parameters.ErrorDisconnectingLocalhost;

        user.context.changeLocation("localhost");

        return "Disconnected";
    }

    /**
     * disconnect command
     * @return disconnects from a domain
     */
    public String dc() {
        return main();
    }
}
