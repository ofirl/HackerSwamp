package commands;

import managers.CommandManager;
import managers.DomainsManager;
import objects.Argument;
import objects.Command;
import objects.CommandContext;
import objects.Parameters;

import java.util.HashMap;

/**
 * BcMiner Program
 */
public class BcMiner extends BaseCommand {
    public static Command superCommand;
    public static HashMap<String, HashMap<String, Argument>> acceptedArguments = new HashMap<>();

    static {
        // super command
        superCommand = CommandManager.allCommands.get(Parameters.CommandNameBcMiner);

        // sub commands hash maps init
        acceptedArguments.put("bcminer", new HashMap<>());
        //acceptedArguments.put("view", new HashMap<>());
        //acceptedArguments.put("delete", new HashMap<>());

        // view
        //acceptedArguments.get("view").put("arg1", new Argument("arg1", int.class));
        //acceptedArguments.get("view").put("count", new Argument("count", int.class));

        // delete
        //acceptedArguments.get("delete").put("arg1", new Argument("arg1", String.class));
        //acceptedArguments.get("delete").put("count", new Argument("count", int.class));
    }

    /**
     * empty constructor for the
     */
    public BcMiner() {
        this(null);
    }

    /**
     * constructor
     * @param context the context to run in
     */
    public BcMiner(CommandContext context) {
        super(context, Parameters.CommandNameBcMiner);
    }

    /**
     * implementation of the abstract method
     * @param context the context of the new instance
     * @return a new instance with the given context
     */
    public BcMiner createInstance(CommandContext context) {
        return new BcMiner(context);
    }

    /**
     * bcminer command
     * @return
     */
    public String main() {
        // TODO : implement bc miner collection!
        return "Implement!";
    }
}
