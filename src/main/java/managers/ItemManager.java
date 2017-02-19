package managers;

import database_objects.*;
import interface_objects.DatabaseHandler;
import interface_objects.DatabaseTables;
import items.*;
import objects.Account;
import objects.Parameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * manages all the items for better performance
 */
public class ItemManager {
    // static variables (keys are items ids)
    public static HashMap<Integer, BaseItem> allItems = new HashMap<>();
    public static HashMap<Integer, Motherboard> motherboards = new HashMap<>();
    public static HashMap<Integer, Cpu> cpus = new HashMap<>();
    public static HashMap<Integer, Ram> rams = new HashMap<>();
    public static HashMap<Integer, Hdd> hdds = new HashMap<>();
    public static HashMap<Integer, NetworkCard> networkCards = new HashMap<>();
    // TODO : add tags to market scripts, like categories or something
    public static HashMap<Integer, MarketScript> marketScripts = new HashMap<>();

    /**
     * init
     */
    public static void init() {
        // motherboards
        List<MotherboardsTableRow> motherboardItems = DatabaseHandler.getTableElements(DatabaseTables.Motherboards);
        for (MotherboardsTableRow i :
                motherboardItems) {
            Motherboard motherboard = new Motherboard(i.id, i.name, i.cpu_slots, i.ram_slots, i.hdd_slots, i.max_ram_size, i.max_cpu_speed);
            motherboards.put(motherboard.id, motherboard);
            allItems.put(motherboard.id, motherboard);
        }

        // cpus
        List<CpusTableRow> cpuItems = DatabaseHandler.getTableElements(DatabaseTables.Cpus);
        for (CpusTableRow i :
                cpuItems) {
            Cpu cpu = new Cpu(i.id, i.name, i.speed, i.cores);
            cpus.put(cpu.id, cpu);
            allItems.put(cpu.id, cpu);
        }

        // rams
        List<RamsTableRow> ramItems = DatabaseHandler.getTableElements(DatabaseTables.Rams);
        for (RamsTableRow i :
                ramItems) {
            Ram ram = new Ram(i.id, i.name, i.size);
            rams.put(ram.id, ram);
            allItems.put(ram.id, ram);
        }

        // hdds
        List<HddsTableRow> hddItems = DatabaseHandler.getTableElements(DatabaseTables.Hdds);
        for (HddsTableRow i :
                hddItems) {
            Hdd hdd = new Hdd(i.id, i.name, i.size);
            hdds.put(hdd.id, hdd);
            allItems.put(hdd.id, hdd);
        }

        // network cards
        List<NetworkcardsTableRow> networkCardItems = DatabaseHandler.getTableElements(DatabaseTables.NetworkCards);
        for (NetworkcardsTableRow i :
                networkCardItems) {
            NetworkCard networkCard = new NetworkCard(i.id, i.name, i.download, i.upload);
            networkCards.put(networkCard.id, networkCard);
            allItems.put(networkCard.id, networkCard);
        }
    }

    /**
     * gets the item with the provided {@code id}
     * @param id the id to search for
     * @return the item object or null if not found
     */
    @SuppressWarnings("unchecked")
    public static <T> T getItemById(int id) {
        BaseItem item = allItems.get(id);
        if (item != null)
            return (T)item;

        return null;
    }

    /**
     * gets the item with the provided {@code name}
     * @param name the name to search for
     * @return the item object or null if not found
     */
    @SuppressWarnings("unchecked")
    public static <T> T getItemByName(String name) {
        for (BaseItem b :
                allItems.values()) {
            if (b.name.equals(name))
                return (T)b;
        }

        return null;
    }

    /**
     * gets all the items
     * @return all items
     */
    public static HashMap<Integer, BaseItem> getAllItems() {
        return allItems;
    }

    /**
     * gets all the motherboards
     * @return all motherboards
     */
    public static HashMap<Integer, Motherboard> getAllMotherboards() {
        return motherboards;
    }

    /**
     * gets all the cpus
     * @return all cpus
     */
    public static HashMap<Integer, Cpu> getAllCpus() {
        return cpus;
    }

    /**
     * gets all the rams
     * @return all rams
     */
    public static HashMap<Integer, Ram> getAllRams() {
        return rams;
    }

    /**
     * gets all the hdds
     * @return all hdds
     */
    public static HashMap<Integer, Hdd> getAllHdds() {
        return hdds;
    }

    /**
     * gets all the network cards
     * @return all network cards
     */
    public static HashMap<Integer, NetworkCard> getAllNetworkCards() {
        return networkCards;
    }

    /**
     * gets all the market scripts
     * @return all market scripts
     */
    public static HashMap<Integer, MarketScript> getAllMarketScripts() {
        return marketScripts;
    }

    /**
     * gets the script with the given {@code name} (name is from the form of "owner.script")
     * @param name the name to search for
     * @return a marketScript object, or null if there isn't one
     */
    public static MarketScript getMarketScriptByName(List<String> command) {
        if (command.size() != 2)
            return null;

        for (MarketScript m :
                marketScripts.values()) {
            if (m.owner.equals(command.get(0)) && m.name.equals(command.get(1)))
                return m;
        }

        return null;
    }

    /**
     * gets the script with the given {@code name} (name is from the form of "owner.script")
     * @param name the name to search for
     * @return a marketScript object, or null if there isn't one
     */
    public static MarketScript getMarketScriptByName(String name) {
        String[] nameParts = name.split(".");
        if (nameParts.length != 2)
            return null;

        List<String> commands = new ArrayList<>();
        commands.add(nameParts[0]);
        commands.add(nameParts[1]);
        return getMarketScriptByName(commands);
    }

    /**
     * attempts to buy the provided {@code scriptId} with the {@code username} main account
     * @param scriptId the id of the script to buy
     * @param username the username who wants to buy the script
     * @return whether the transaction was successful : "OK" or "Error : ... "
     */
    public static String attemptBuyScript(int scriptId, String username) {
        MarketScript script = marketScripts.get(scriptId);
        if (script == null)
            return Parameters.ErrorScriptNotFound;

        Account account = DomainsManager.getMainAccountByUsername(username);
        if (account == null)
            return Parameters.ErrorMainAccountNotFound;

        if (!account.canTransfer(script.price))
            return Parameters.ErrorInsufficientFunds;

        // main account for script owner will never be null,
        // to put something on the market you got to have a main account
        //noinspection ConstantConditions
        DomainsManager.getMainAccountByUsername(script.owner).changeBalance(script.price);
        account.changeBalance(-script.price);

        return "OK";
    }
}
