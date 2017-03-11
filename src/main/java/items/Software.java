package items;

import objects.Command;

import java.util.HashMap;

public class Software extends MarketScript {
    public double version;
    public double hidden;
    public String location;
    public int rowId;
    public String costumeName;
    public boolean installed;

    /**
     * constructor
     */
    public Software(int id, String name, Command command, int price, String creator, MarketScriptType type, double size, double version, double hidden, int rowId, String costumeName, boolean installed) {
        super(id, name, command, price, creator, type, size);
        this.version = version;
        this.hidden = hidden;
        this.rowId = rowId;
        this.costumeName = costumeName;
        this.installed = installed;
    }

    /**
     * constructor
     */
    public Software(int id, String name, Command command, int price, String creator, MarketScriptType type, double size) {
        this(id, name, command, price, creator, type, size, 0, 0, 0, name, false);
    }

    /**
     * constructor
     */
    public Software(Software software, String location, double version, double hidden, int rowId, String costumeName, boolean installed) {
        // TODO : change to use the first constructor
        super(software);

        this.location = location;
        this.version = version;
        this.hidden = hidden;
        this.rowId = rowId;
        this.costumeName = costumeName;
        this.installed = installed;
    }

    /**
     * copy constructor
     */
    public Software(Software software) {
        this (software, software.location, software.version, software.hidden, software.rowId, software.costumeName, software.installed);
    }

    /**
     * constructor
     */
    public Software(Software software, String costumeName) {
        this (software, software.location, software.version, software.hidden, software.rowId, costumeName, software.installed);
    }

    /**
     * creates an inventory entry (filling in rowId)
     * @param rowId row id to add
     * @return software inventory entry
     */
    public Software createInventoryEntry(int rowId, String costumeName, boolean installed) {
        Software entry = new Software(this);
        entry.rowId = rowId;
        entry.costumeName = costumeName;
        entry.installed = installed;

        return entry;
    }

    /**
     * gets the software as arguments to send
     * @return the software as arguments
     */
    @Override
    public HashMap<String, String> getSpecAsArguments() {
        HashMap<String, String> cmd = super.getSpecAsArguments();

        cmd.put("market_script_version", String.valueOf(version));
        cmd.put("market_script_hidden", String.valueOf(hidden));

        return cmd;
    }

    /**
     * detailed info about the item
     */
    @Override
    public String toString() {
        String output = "";

        output += super.toString() + "\n";
        output += "Version : " + version + "\n";
        output += "Hidden : " + hidden + "\n";
        output += "Location : " + location;

        return output;
    }
}
