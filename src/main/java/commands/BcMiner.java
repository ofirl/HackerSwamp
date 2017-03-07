package commands;

import managers.CommandManager;
import managers.DomainsManager;
import objects.Argument;
import objects.Command;
import objects.CommandContext;
import objects.Parameters;

import java.util.HashMap;

/**
 * bcminer Program
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

        // bcminer
        acceptedArguments.get("bcminer").put("installedUsername", new Argument("installedUsername", String.class));
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
        // check for invalid argument
        if (!checkArguments(acceptedArguments.get("bcminer")))
            return Parameters.ErrorCommandInvalidArguments;

        String output = "";

        output += args.get("installedUsername").value;

        // TODO : implement bc miner collection!
        return output;
    }
}
