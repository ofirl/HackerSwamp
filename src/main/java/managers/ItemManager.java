package managers;

import items.*;

import java.util.HashMap;

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

    /**
     * init
     */
    public static void init() {
        //TODO : implement
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
}
