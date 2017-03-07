package commands;

import interface_objects.LoginHandler;
import items.Software;
import managers.CommandManager;
import objects.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Collect extends BaseCommand {
    public static Command superCommand;
    public static HashMap<String, HashMap<String, Argument>> acceptedArguments = new HashMap<>();

    static {
        // super command
        superCommand = CommandManager.allCommands.get(Parameters.CommandNameCollect);

        // sub commands hash maps init
        acceptedArguments.put("collect", new HashMap<>());
        //acceptedArguments.put("view", new HashMap<>());
        //acceptedArguments.put("delete", new HashMap<>());

        // collect
        acceptedArguments.get("collect").put("arg1", new Argument("arg1", int.class));
        acceptedArguments.get("collect").put("from", new Argument("from", int.class));
    }

    /**
     * empty constructor for the
     */
    public Collect() {
        this(null);
    }

    /**
     * constructor
     * @param context the context to run in
     */
    public Collect(CommandContext context) {
        super(context, Parameters.CommandNameCollect);
    }

    /**
     * implementation of the abstract method
     * @param context the context of the new instance
     * @return a new instance with the given context
     */
    public Collect createInstance(CommandContext context) {
        return new Collect(context);
    }

    /**
     * collect command
     * @return a response
     */
    public String main() {
        // check for invalid argument
        if (!checkArguments(acceptedArguments.get("collect")))
            return Parameters.ErrorCommandInvalidArguments;

        ActiveUser activeUser = LoginHandler.getActiveUserByUsername(context.username);
        if (activeUser == null)
            return Parameters.ErrorActiveUserNotFound;

        String output = "";
        HashMap<Integer, Software> installed = activeUser.getInstalledSoftware();
        for (Software s :
                installed.values()) {
            switch (s.type) {
                case bcminer:
                    Command cmd = CommandManager.getCommandById(s.command.id);
                    if (cmd == null)
                        return "Error";

                    List<Argument> cmdArgs = new ArrayList<>();
                    cmdArgs.add(new Argument("installedUsername", String.class, s.location));
                    output += cmd.execute(context, Parameters.CommandNameBcMiner, cmdArgs);
                    break;
            }
        }

        return output;
    }
}
