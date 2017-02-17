package objects;

import database_objects.EquippedItemsTableRow;
import interface_objects.DatabaseHandler;
import interface_objects.DatabaseTables;
import items.*;
import managers.ItemManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * implements system specs
 */
public class SystemSpec {
    // public variables
    public Motherboard motherboard;
    public List<Cpu> cpus;
    public List<Ram> rams;
    public List<Hdd> hdds;
    public NetworkCard networkCard;

    /**
     * constructor
     */
    public SystemSpec(Motherboard motherboard, List<Cpu> cpus, List<Ram> rams, List<Hdd> hdds, NetworkCard networkCard) {
        this.motherboard = motherboard;
        this.cpus = cpus;
        this.rams = rams;
        this.hdds = hdds;
        this.networkCard = networkCard;
    }

    /**
     * gets the provided {@code username} system spec
     * @param username the username to search for
     * @return the system spec of the user
     */
    public static SystemSpec getUserSystemSpecs(String username) {
        List<EquippedItemsTableRow> equippedItems = DatabaseHandler.getTableElements(DatabaseTables.Equipped_Items, "itemid, type", "owner='" + username + "'");
        if (equippedItems == null)
            return null;

        Motherboard motherboard = null;
        List<Cpu> cpus = new ArrayList<>();
        List<Ram> rams = new ArrayList<>();
        List<Hdd> hdds = new ArrayList<>();
        NetworkCard networkCard = null;

        for (EquippedItemsTableRow e :
                equippedItems) {
            switch (e.type) {
                case "motherboards":
                    motherboard = ItemManager.getItemById(e.itemid);
                    break;
                case "cpus":
                    cpus.add(ItemManager.getItemById(e.itemid));
                    break;
                case "rams":
                    rams.add(ItemManager.getItemById(e.itemid));
                    break;
                case "hdds":
                    hdds.add(ItemManager.getItemById(e.itemid));
                    break;
                case "networkcards":
                    networkCard = ItemManager.getItemById(e.itemid);
                    break;
            }
        }

        return new SystemSpec(motherboard, cpus, rams, hdds, networkCard);
    }

    /**
     * gets the system spec as arguments to send
     * @return the system spec as arguments
     */
    public HashMap<String, String> getSpecAsArguments() {
        HashMap<String, String> spec = new HashMap<>();

        // motherboard
        spec.putAll(motherboard.getSpecAsArguments());

        // network card
        spec.putAll(networkCard.getSpecAsArguments());

        // cpu
        for (Cpu c :
                cpus)
            spec.putAll(c.getSpecAsArguments());

        // ram
        for (Ram r :
                rams)
            spec.putAll(r.getSpecAsArguments());

        // hdd
        for (Hdd h :
                hdds)
            spec.putAll(h.getSpecAsArguments());

        return spec;
    }

    /**
     * gets a detailed and pretty print of the system spec object
     * @return a detailed print
     */
    public String getDetailedPrint() {
        // TODO : implement
        return "implement!";
    }
}
