package commands;

import domains.BaseDomain;
import interface_objects.LoginHandler;
import items.Software;
import managers.CommandManager;
import managers.DomainsManager;
import objects.*;

import java.util.HashMap;

public class Delete extends BaseCommand {
    public static Command superCommand;
    public static HashMap<String, HashMap<String, Argument>> acceptedArguments = new HashMap<>();

    static {
        // super command
        superCommand = CommandManager.getCommandByName(Parameters.CommandNameDelete);

        // sub commands hash maps init
        acceptedArguments.put("delete", new HashMap<>());

        // install
        acceptedArguments.get("delete").put("arg1", new Argument("arg1", String.class));
        acceptedArguments.get("delete").put("program", new Argument("program", String.class));
    }

    /**
     * empty constructor for the
     */
    public Delete() {
        this(null);
    }

    /**
     * constructor
     * @param context the context to run in
     */
    public Delete(CommandContext context) {
        super(context, Parameters.CommandNameDelete);
    }

    /**
     * implementation of the abstract method
     * @param context the context of the new instance
     * @return a new instance with the given context
     */
    public Delete createInstance(CommandContext context) {
        return new Delete(context);
    }

    /**
     * delete command
     * @return general help
     */
    public String main() {
        // check for invalid argument
        if (!checkArguments(acceptedArguments.get("delete"))) {
            return Parameters.ErrorCommandInvalidArguments;
        }

        // make sure we have a program name to delete
        String softwareName = null;
        if (args.containsKey("arg1"))
            softwareName = args.get("arg1").castValue(String.class);
        else if (args.containsKey("program"))
            softwareName = args.get("program").castValue(String.class);
        else
            return Parameters.CommandUsageDelete;

        boolean domainLocation = true;
        String deleteLocation = context.location;
        HashMap<Integer, Software> inventory = null;

        // get the current location inventory
        if (context.location.equals("localhost")) {
            domainLocation = false;
            deleteLocation = context.username;
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
            if (b.costumeName.equals(softwareName))
                foundSoftware = b;
        }

        if (foundSoftware == null)
            return Parameters.ErrorSoftwareNotFoundInInventory;

        if (foundSoftware.installed)
            return "Error : cannot delete installed program, uninstall it first";

        // TODO : add timer (based on cpu speed?)
        if (domainLocation)
            DomainsManager.getDomainByName(deleteLocation).deleteSoftware(foundSoftware);
        else
            // TODO : implement install at user
            domainLocation = false;

        return "program deleted";
    }
}
