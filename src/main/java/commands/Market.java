package commands;

import items.BaseItem;
import managers.CommandManager;
import managers.ItemManager;
import managers.Logger;
import objects.*;

import java.util.*;

public class Market extends BaseCommand {

    public static Command superCommand;
    public static HashMap<String, HashMap<String, Argument>> acceptedArguments = new HashMap<>();

    static {
        // super command
        superCommand = CommandManager.allCommands.get(Parameters.CommandNameMarket);

        // sub commands hash maps init
        acceptedArguments.put("market", new HashMap<>());
        acceptedArguments.put("items", new HashMap<>());
        acceptedArguments.put("scripts", new HashMap<>());

        // items
        acceptedArguments.get("items").put("type", new Argument("type", String.class));
        acceptedArguments.get("items").put("buy", new Argument("buy", int.class));

        // scripts
        acceptedArguments.get("scripts").put("security", new Argument("security", String.class));
        acceptedArguments.get("scripts").put("buy", new Argument("buy", int.class));
    }

    /**
     * empty constructor for the
     */
    public Market() {
        this(null);
    }

    /**
     * constructor
     * @param context the context to run in
     */
    public Market(CommandContext context) {
        super(context, Parameters.CommandNameMarket);
    }

    /**
     * implementation of the abstract method
     * @param context the context of the new instance
     * @return a new instance with the given context
     */
    public Market createInstance(CommandContext context) {
        return new Market(context);
    }

    /**
     * help command
     * @return general help
     */
    public String main() {
        // TODO : return general help
        return getSubCommands(superCommand);
    }

    /**
     * market.items command
     * @return list of items
     */
    public String items() {
        // check for invalid argument
        HashMap<String, Argument> acceptedCommandArgs = acceptedArguments.get("items");
        for (String arg :
                args.keySet()) {
            if (!acceptedCommandArgs.containsKey(arg) || acceptedCommandArgs.get(arg).type != args.get(arg).type)
                return Parameters.ErrorCommandInvalidArguments;
        }

        String output = "";
        HashMap<Integer, BaseItem> items = new HashMap<>();

        if (args.containsKey("type")) {
            String itemType = args.get("type").value;
            switch (itemType) {
                case "motherboard" :
                    items = ItemManager.castItemsToBaseItem(ItemManager.getAllMotherboards());
                    break;
                case "cpu" :
                    items = ItemManager.castItemsToBaseItem(ItemManager.getAllCpus());
                    break;
                case "ram" :
                    items = ItemManager.castItemsToBaseItem(ItemManager.getAllRams());
                    break;
                case "hdd" :
                    items = ItemManager.castItemsToBaseItem(ItemManager.getAllHdds());
                    break;
                case "network" :
                    items = ItemManager.castItemsToBaseItem(ItemManager.getAllNetworkCards());
                    break;
                default :
                    output += "accepted values for \"type\" are : motherboard, cpu, ram, hdd and network";
            }
        }
        else
            items = ItemManager.getAllItems();

        if (items == null) {
            String msg = "Error retrieving items for type = " + args.get("type").value + ", username = " + context.username;
            Logger.log("Market.items", msg);
            return Parameters.ErrorUnknownError;
        }

        for (BaseItem item :
                items.values())
            output += item.toString() + "\n\n";

        return output;
    }

    /**
     * market.scripts command
     * @return list of scripts
     */
    public String scripts() {
        // check for invalid argument
        HashMap<String, Argument> acceptedCommandArgs = acceptedArguments.get("items");
        for (String arg :
                args.keySet()) {
            if (!acceptedCommandArgs.containsKey(arg) || acceptedCommandArgs.get(arg).type != args.get(arg).type)
                return Parameters.ErrorCommandInvalidArguments;
        }

        String output = "";
        HashMap<Integer, BaseItem> items = new HashMap<>();

        if (args.containsKey("security")) {
            String securityLevel = args.get("security").value;
            switch (CommandSecurityRating.valueOf(securityLevel)) {
                case Unknown:
                    items.putAll(ItemManager.castItemsToBaseItem(ItemManager.getAllMarketScripts(CommandSecurityRating.Unknown)));
                case LowSec :
                    items.putAll(ItemManager.castItemsToBaseItem(ItemManager.getAllMarketScripts(CommandSecurityRating.LowSec)));
                case MedSec:
                    items.putAll(ItemManager.castItemsToBaseItem(ItemManager.getAllMarketScripts(CommandSecurityRating.MedSec)));
                case TopSec :
                    items.putAll(ItemManager.castItemsToBaseItem(ItemManager.getAllMarketScripts(CommandSecurityRating.TopSec)));
                case System :
                    items.putAll(ItemManager.castItemsToBaseItem(ItemManager.getAllMarketScripts(CommandSecurityRating.System)));
                    break;
                default :
                    return "accepted values for \"security\" are : System, TopSec, MedSec, LowSec, Unknown";
            }
        }
        else
            items = ItemManager.getAllItems();

        if (items == null) {
            String msg = "Error retrieving items for type = " + args.get("type").value + ", username = " + context.username;
            Logger.log("Market.items", msg);
            return Parameters.ErrorUnknownError;
        }

        for (BaseItem item :
                items.values())
            output += item.toString() + "\n\n";

        return output;
    }
}
