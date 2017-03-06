package commands;

import interface_objects.DatabaseHandler;
import interface_objects.DatabaseTables;
import interface_objects.LoginHandler;
import items.BaseItem;
import managers.CommandManager;
import managers.DomainsManager;
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
     * buys the item
     * @return a response
     */
    public String buyItem() {
        int itemId = args.get("buy").castValue(Integer.class);
        // sanity checks
        BaseItem selectedItem = ItemManager.getItemById(itemId);
        if (selectedItem == null)
            return Parameters.ErrorMarketItemNotFound;

        ActiveUser user = LoginHandler.getActiveUserByUsername(context.username);
        if (user == null)
            return Parameters.ErrorActiveUserNotFound;

        Account acc = user.getMainAccount();
        if (acc == null)
            return Parameters.ErrorMainAccountNotFound;

        if (!acc.canTransfer(selectedItem.price))
            return Parameters.ErrorInsufficientFunds;

        // subtract the item price if bought successfully
        if (ItemManager.addItemToUserInventory(context.username, selectedItem.id)) {
            acc.changeBalance(-selectedItem.price);
            user.getInventory().put(itemId, selectedItem);
            return "Item has been bought";
        }

        return "Could not finalize the order";
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
        if (!checkArguments(acceptedArguments.get("items")))
            return Parameters.ErrorCommandInvalidArguments;

        String output = "";
        HashMap<Integer, BaseItem> items = new HashMap<>();

        if (args.containsKey("buy"))
            return buyItem();

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
        if (!checkArguments(acceptedArguments.get("scripts")))
            return Parameters.ErrorCommandInvalidArguments;

        String output = "";
        HashMap<Integer, BaseItem> items = new HashMap<>();

        if (args.containsKey("buy"))
            return buyItem();

        if (args.containsKey("security")) {
            String securityLevel = args.get("security").value;
            switch (CommandSecurityRating.valueOf(securityLevel)) {
                case unknown:
                    items.putAll(ItemManager.castItemsToBaseItem(ItemManager.getAllMarketScripts(CommandSecurityRating.unknown)));
                case lowsec:
                    items.putAll(ItemManager.castItemsToBaseItem(ItemManager.getAllMarketScripts(CommandSecurityRating.lowsec)));
                case medsec:
                    items.putAll(ItemManager.castItemsToBaseItem(ItemManager.getAllMarketScripts(CommandSecurityRating.medsec)));
                case topsec:
                    items.putAll(ItemManager.castItemsToBaseItem(ItemManager.getAllMarketScripts(CommandSecurityRating.topsec)));
                case syscmd:
                    items.putAll(ItemManager.castItemsToBaseItem(ItemManager.getAllMarketScripts(CommandSecurityRating.syscmd)));
                    break;
                default :
                    return "accepted values for \"security\" are : syscmd, topsec, medsec, lowsec, unknown";
            }
        }
        else
            items = ItemManager.castItemsToBaseItem(ItemManager.getAllMarketScripts());

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
