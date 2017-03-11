package commands;

import domains.BaseDomain;
import interface_objects.LoginHandler;
import items.Software;
import managers.CommandManager;
import managers.DomainsManager;
import objects.*;

import java.util.HashMap;

public class Upload extends BaseCommand {
    public static Command superCommand;
    public static HashMap<String, HashMap<String, Argument>> acceptedArguments = new HashMap<>();

    static {
        // super command
        superCommand = CommandManager.getCommandByName(Parameters.CommandNameUpload);

        // sub commands hash maps init
        acceptedArguments.put("upload", new HashMap<>());

        // install
        acceptedArguments.get("upload").put("arg1", new Argument("arg1", String.class));
        acceptedArguments.get("upload").put("program", new Argument("program", String.class));
        acceptedArguments.get("upload").put("arg2", new Argument("arg2", String.class));
        acceptedArguments.get("upload").put("newName", new Argument("newName", String.class));
    }

    /**
     * empty constructor for the
     */
    public Upload() {
        this(null);
    }

    /**
     * constructor
     * @param context the context to run in
     */
    public Upload(CommandContext context) {
        super(context, Parameters.CommandNameUpload);
    }

    /**
     * implementation of the abstract method
     * @param context the context of the new instance
     * @return a new instance with the given context
     */
    public Upload createInstance(CommandContext context) {
        return new Upload(context);
    }

    /**
     * upload command
     * @return general help
     */
    public String main() {
        // check for invalid argument
        if (!checkArguments(acceptedArguments.get("upload"))) {
            return Parameters.ErrorCommandInvalidArguments;
        }

        // make sure we have a program name to copy and optionally a new name
        String softwareName = null;
        if (args.containsKey("arg1"))
            softwareName = args.get("arg1").castValue(String.class);
        else if (args.containsKey("program"))
            softwareName = args.get("program").castValue(String.class);
        else
            return Parameters.CommandUsageUpload;

        String newName = null;
        if (args.containsKey("arg2"))
            newName = args.get("arg2").castValue(String.class);
        else if (args.containsKey("newName"))
            newName = args.get("newName").castValue(String.class);
        else
            newName = softwareName;

        if (context.location.equals("localhost"))
            return Parameters.ErrorUploadLocalHost;

        String uploadLocation = context.location;

        // get the current location inventory
        BaseDomain domain = DomainsManager.getDomainByName(uploadLocation);
        if (domain == null)
            return Parameters.ErrorDomainNotFoundPrefix;

        ActiveUser activeUser = LoginHandler.getActiveUserByUsername(context.username);
        if (activeUser == null)
            return Parameters.ErrorActiveUserNotFound;

        // TODO : change, get only the software at the location
        HashMap<Integer, Software> sourceInventory = activeUser.getSoftwareInventory();
        // make sure the program is in the source inventory
        Software foundSoftware = null;
        for (Software s :
                sourceInventory.values())
            if (s.costumeName.equals(softwareName)) {
                foundSoftware = s;
                break;
            }

        if (foundSoftware == null)
            return Parameters.ErrorSoftwareNotFoundInInventory;

        // TODO : change, get only the software at the location
        HashMap<Integer, Software> targetInventory = domain.getSoftwareInventory();

        // make sure the program is not in the target inventory
        for (Software s :
                targetInventory.values())
            if (s.costumeName.equals(newName))
                return "Error : program with the name " + newName + " already exists";

        // make sure there is enough space
        int availableSpace = DomainsManager.getDomainByName(uploadLocation).getAvailableSize();
        if (foundSoftware.size > availableSpace)
            return Parameters.ErrorNotEnoughFreeSpace;

        // TODO : add restriction : can't upload installed software that you don't own

        // TODO : add timer (based on upload speed?)
        DomainsManager.getDomainByName(uploadLocation).addSoftware(new Software(foundSoftware, newName), context.username);

        return "program uploaded";
    }
}
