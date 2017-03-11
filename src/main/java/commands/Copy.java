package commands;

import domains.BaseDomain;
import interface_objects.LoginHandler;
import items.Software;
import managers.CommandManager;
import managers.DomainsManager;
import objects.*;

import java.util.HashMap;

public class Copy extends BaseCommand {
    public static Command superCommand;
    public static HashMap<String, HashMap<String, Argument>> acceptedArguments = new HashMap<>();

    static {
        // super command
        superCommand = CommandManager.getCommandByName(Parameters.CommandNameCopy);

        // sub commands hash maps init
        acceptedArguments.put("copy", new HashMap<>());

        // install
        acceptedArguments.get("copy").put("arg1", new Argument("arg1", String.class));
        acceptedArguments.get("copy").put("program", new Argument("program", String.class));
        acceptedArguments.get("copy").put("arg2", new Argument("arg2", String.class));
        acceptedArguments.get("copy").put("newName", new Argument("newName", String.class));
    }

    /**
     * empty constructor for the
     */
    public Copy() {
        this(null);
    }

    /**
     * constructor
     * @param context the context to run in
     */
    public Copy(CommandContext context) {
        super(context, Parameters.CommandNameCopy);
    }

    /**
     * implementation of the abstract method
     * @param context the context of the new instance
     * @return a new instance with the given context
     */
    public Copy createInstance(CommandContext context) {
        return new Copy(context);
    }

    /**
     * copy command
     * @return general help
     */
    public String main() {
        // check for invalid argument
        if (!checkArguments(acceptedArguments.get("copy"))) {
            return Parameters.ErrorCommandInvalidArguments;
        }

        // make sure we have a program name to copy and a new name
        if (args.size() != 2)
            return Parameters.CommandUsageCopy;

        String softwareName = null;
        if (args.containsKey("arg1"))
            softwareName = args.get("arg1").castValue(String.class);
        else if (args.containsKey("program"))
            softwareName = args.get("program").castValue(String.class);
        else
            return Parameters.CommandUsageCopy;

        String newName = null;
        if (args.containsKey("arg2"))
            newName = args.get("arg2").castValue(String.class);
        else if (args.containsKey("newName"))
            newName = args.get("newName").castValue(String.class);
        else
            return Parameters.CommandUsageCopy;

        boolean domainLocation = true;
        String copyLocation = context.location;
        HashMap<Integer, Software> inventory = null;

        // get the current location inventory
        if (context.location.equals("localhost")) {
            domainLocation = false;
            copyLocation = context.username;
            ActiveUser activeUser = LoginHandler.getActiveUserByUsername(context.username);
            if (activeUser == null)
                return Parameters.ErrorActiveUserNotFound;

            // TODO : change, get only the software at the location
            inventory = activeUser.getSoftwareInventory();
        }
        else {
            BaseDomain domain = DomainsManager.getDomainByName(context.location);
            if (domain == null)
                return Parameters.ErrorDomainNotFoundPrefix;

            inventory = domain.getSoftwareInventory();
        }

        // make sure the program is in the found inventory
        Software foundSoftware = null;
        for (Software b :
                inventory.values()) {
            if (b.costumeName.equals(newName))
                return "Error : program with the name " + newName + " already exists";
            if (b.costumeName.equals(softwareName))
                foundSoftware = b;
        }

        if (foundSoftware == null)
            return Parameters.ErrorSoftwareNotFoundInInventory;

        // make sure there is enough space
        int availableSpace = DomainsManager.getDomainByName(copyLocation).getAvailableSize();
        if (foundSoftware.size > availableSpace)
            return "Error : not enough free space";

        // TODO : add timer (based on cpu speed?)
        if (domainLocation)
            DomainsManager.getDomainByName(copyLocation).addSoftware(foundSoftware, context.username);
        else
            // TODO : implement install at user
            domainLocation = false;

        return "program copied";
    }
}
