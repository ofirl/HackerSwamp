package commands;

import domains.BaseDomain;
import interface_objects.LoginHandler;
import items.Software;
import managers.CommandManager;
import managers.DomainsManager;
import objects.*;

import java.util.HashMap;

public class Uninstall extends BaseCommand {
    public static Command superCommand;
    public static HashMap<String, HashMap<String, Argument>> acceptedArguments = new HashMap<>();

    static {
        // super command
        superCommand = CommandManager.allCommands.get(Parameters.CommandNameUninstall);

        // sub commands hash maps init
        acceptedArguments.put("uninstall", new HashMap<>());
        //acceptedArguments.put("view", new HashMap<>());
        //acceptedArguments.put("delete", new HashMap<>());

        // install
        acceptedArguments.get("uninstall").put("arg1", new Argument("arg1", String.class));
        acceptedArguments.get("uninstall").put("program", new Argument("program", String.class));

        // delete
        //acceptedArguments.get("delete").put("arg1", new Argument("arg1", String.class));
        //acceptedArguments.get("delete").put("count", new Argument("count", int.class));
    }

    /**
     * empty constructor for the
     */
    public Uninstall() {
        this(null);
    }

    /**
     * constructor
     * @param context the context to run in
     */
    public Uninstall(CommandContext context) {
        super(context, Parameters.CommandNameUninstall);
    }

    /**
     * implementation of the abstract method
     * @param context the context of the new instance
     * @return a new instance with the given context
     */
    public Uninstall createInstance(CommandContext context) {
        return new Uninstall(context);
    }

    /**
     * uninstall command
     * @return general help
     */
    public String main() {
        // check for invalid argument
        if (!checkArguments(acceptedArguments.get("uninstall"))) {
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

            // TODO : change, get only the software at the location
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
            return Parameters.ErrorSoftwareNotFoundInInventory;

        if (!foundSoftware.installed)
            return Parameters.ErrorSoftwareNotInstalled;

        // TODO : move to copy command
        /*
        // make sure there is enough space
        int availableSpace = DomainsManager.getDomainByName(installLocation).getAvailableSize();
        if (foundSoftware.size > availableSpace)
            return "Error : not enough free space";
        */

        // TODO : add timer (based on cpu speed?)
        if (domainLocation)
            DomainsManager.getDomainByName(installLocation).uninstallSoftware(foundSoftware);
        else
            // TODO : implement install at user
            domainLocation = false;

        return "program installed";
    }
}
