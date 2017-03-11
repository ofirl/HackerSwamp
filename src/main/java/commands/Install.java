package commands;

import domains.BaseDomain;
import interface_objects.LoginHandler;
import items.Software;
import managers.CommandManager;
import managers.DomainsManager;
import objects.*;

import java.util.HashMap;

public class Install extends BaseCommand {
    public static Command superCommand;
    public static HashMap<String, HashMap<String, Argument>> acceptedArguments = new HashMap<>();

    static {
        // super command
        superCommand = CommandManager.allCommands.get(Parameters.CommandNameInstall);

        // sub commands hash maps init
        acceptedArguments.put("install", new HashMap<>());
        //acceptedArguments.put("view", new HashMap<>());
        //acceptedArguments.put("delete", new HashMap<>());

        // install
        acceptedArguments.get("install").put("arg1", new Argument("arg1", String.class));
        acceptedArguments.get("install").put("program", new Argument("program", String.class));
        acceptedArguments.get("install").put("arg2", new Argument("arg2", int.class));
        acceptedArguments.get("install").put("version", new Argument("version", int.class));

        // delete
        //acceptedArguments.get("delete").put("arg1", new Argument("arg1", String.class));
        //acceptedArguments.get("delete").put("count", new Argument("count", int.class));
    }

    /**
     * empty constructor for the
     */
    public Install() {
        this(null);
    }

    /**
     * constructor
     * @param context the context to run in
     */
    public Install(CommandContext context) {
        super(context, Parameters.CommandNameInstall);
    }

    /**
     * implementation of the abstract method
     * @param context the context of the new instance
     * @return a new instance with the given context
     */
    public Install createInstance(CommandContext context) {
        return new Install(context);
    }

    /**
     * install command
     * @return general help
     */
    public String main() {
        // check for invalid argument
        if (!checkArguments(acceptedArguments.get("install"))) {
            return Parameters.ErrorCommandInvalidArguments;
        }

        // make sure we have a program name to install
        String softwareName = null;
        if (args.containsKey("arg1"))
            softwareName = args.get("arg1").castValue(String.class);
        else if (args.containsKey("software"))
            softwareName = args.get("program").castValue(String.class);
        else
            return Parameters.CommandUsageInstall;


        boolean domainLocation = true;
        String installLocation = context.location;
        HashMap<Integer, Software> inventory = null;

        // get the current location inventory
        if (context.location.equals("localhost")) {
            domainLocation = false;
            installLocation = context.username;
            ActiveUser activeUser = LoginHandler.getActiveUserByUsername(context.username);
            if (activeUser == null)
                return Parameters.ErrorActiveUserNotFound;

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
                inventory.values())
            if (b.costumeName.equals(softwareName)) {
                foundSoftware = b;
                break;
            }

        if (foundSoftware == null)
            return "Error : program not found in inventory";

        if (foundSoftware.installed)
            return "Error : software already installed";

        // TODO : move to copy command
        /*
        // make sure there is enough space
        int availableSpace = DomainsManager.getDomainByName(installLocation).getAvailableSize();
        if (foundSoftware.size > availableSpace)
            return "Error : not enough free space";
        */

        // TODO : add timer (based on cpu speed?)
        if (domainLocation)
            DomainsManager.getDomainByName(installLocation).installSoftware(foundSoftware);
        else
            // TODO : implement install at user
            domainLocation = false;

        return "program installed";
    }
}
