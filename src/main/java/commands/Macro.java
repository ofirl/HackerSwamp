package commands;

import interface_objects.DatabaseHandler;
import interface_objects.DatabaseTables;
import interface_objects.LoginHandler;
import managers.CommandManager;
import objects.*;

import java.util.HashMap;

public class Macro extends BaseCommand {

    public static Command superCommand;
    public static HashMap<String, HashMap<String, Argument>> acceptedArguments = new HashMap<>();

    static {
        // super command
        superCommand = CommandManager.allCommands.get(Parameters.CommandNameMacro);

        // sub commands hash maps init
        acceptedArguments.put("macro", new HashMap<>());
        acceptedArguments.put("add", new HashMap<>());
        acceptedArguments.put("remove", new HashMap<>());
        acceptedArguments.put("view", new HashMap<>());

        // add
        acceptedArguments.get("add").put("arg1", new Argument("add", String.class));
        acceptedArguments.get("add").put("arg2", new Argument("arg2", String.class));

        // remove
        acceptedArguments.get("remove").put("arg1", new Argument("arg1", String.class));

        // view
        acceptedArguments.get("view").put("arg1", new Argument("arg1", String.class));
    }

    /**
     * empty constructor
     */
    public Macro() {
        this(null);
    }

    /**
     * constructor
     * @param context the context to run in
     */
    public Macro(CommandContext context) {
        super(context, Parameters.CommandNameMacro);
    }

    /**
     * implementation of the abstract method
     * @param context the context of the new instance
     * @return a new instance with the given context
     */
    public Macro createInstance(CommandContext context) {
        return new Macro(context);
    }

    /**
     * adds/updates a macro
     * @param macroName macro name to add/update
     * @param macroValue macro value
     * @return a response
     */
    public String addMacro(String macroName, String macroValue) {
        ActiveUser activeUser = LoginHandler.getActiveUserByUsername(context.username);
        if (activeUser == null)
            return Parameters.ErrorActiveUserNotFound;

        // macro exists - update
        if (activeUser.getMacros().containsKey(macroName)) {
            String filter = "player_id=" + context.playerId + " AND macro='" + macroName + "'";
            String values = "command='" + macroValue + "'";
            String prevValue = activeUser.getMacros().get(macroName);

            if (DatabaseHandler.updateTable(DatabaseTables.Macros, filter, values)) {
                activeUser.getMacros().put(macroName, macroValue);
                return "macro " + macroName + " changed from " + prevValue + " to " + macroValue;
            }

            return Parameters.ErrorMacroSetFailed;
        }
        // new macro - add
        else {
            String columnOrder = "player_id, macro, command";
            String values = context.playerId + ",'" + macroName + "','" + macroValue + "'";

            if (DatabaseHandler.insertIntoTable(DatabaseTables.Macros, columnOrder, values)) {
                activeUser.getMacros().put(macroName, macroValue);
                return "macro " + macroName + " added with a value of " + macroValue;
            }

            return Parameters.ErrorMacroSetFailed;
        }
    }

    /**
     * macro command
     * @return general help
     */
    public String main() {
        return getSubCommands(superCommand);
    }

    /**
     * macro.add command
     * @return a response
     */
    public String add() {
        // check for invalid argument
        if (!checkArguments(acceptedArguments.get("add")))
            return Parameters.ErrorCommandInvalidArguments;

        ActiveUser activeUser = LoginHandler.getActiveUserByUsername(context.username);
        if (activeUser == null)
            return Parameters.ErrorActiveUserNotFound;

        if (!args.containsKey("arg1") || !args.containsKey("arg2"))
            return Parameters.CommandUsageMacroAdd;

        return addMacro(args.get("arg1").castValue(), args.get("arg2").castValue());
    }

    /**
     * macro.remove command
     * @return a response
     */
    public String remove() {
        // check for invalid argument
        if (!checkArguments(acceptedArguments.get("remove")))
            return Parameters.ErrorCommandInvalidArguments;

        ActiveUser activeUser = LoginHandler.getActiveUserByUsername(context.username);
        if (activeUser == null)
            return Parameters.ErrorActiveUserNotFound;

        if (!args.containsKey("arg1"))
            return Parameters.CommandUsageMacroRemove;

        String macroName = args.get("arg1").castValue();
        if (activeUser.getMacros().containsKey(macroName)) {
            String filter = "player_id=" + context.playerId + " AND macro='" + macroName + "'";
            DatabaseHandler.removeFromTable(DatabaseTables.Macros, filter);
            activeUser.getMacros().remove(macroName);
            return "macro " + macroName + " has been removed";
        }

        return "macro " + macroName + " does not exists";
    }

    /**
     * macro.view command
     * @return a response
     */
    public String view() {
        // check for invalid argument
        if (!checkArguments(acceptedArguments.get("view")))
            return Parameters.ErrorCommandInvalidArguments;

        ActiveUser activeUser = LoginHandler.getActiveUserByUsername(context.username);
        if (activeUser == null)
            return Parameters.ErrorActiveUserNotFound;

        // all user macros
        HashMap<String, String> macros = activeUser.getMacros();

        if (args.containsKey("arg1")) {
            String name = args.get("arg1").castValue();
            if (macros.containsKey(name))
                return "/" + name + " = " + macros.get(name);
            return Parameters.ErrorMacroNotFound;
        }

        String output = "Macros :";
        for (String name :
                macros.keySet())
            output += "/" + name + " = " + macros.get(name) + "\n";

        return output;
    }
}
