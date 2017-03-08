package items;

import objects.Command;

import java.util.HashMap;

public class Software extends MarketScript {
    public double version;
    public double hidden;
    public String location;

    /**
     * constructor
     */
    public Software(int id, String name, Command command, int price, String creator, MarketScriptType type, double size, double version, double hidden) {
        super(id, name, command, price, creator, type, size);
        this.version = version;
        this.hidden = hidden;
    }

    /**
     * constructor
     */
    public Software(int id, String name, Command command, int price, MarketScriptType type, double size) {
        this(id, name, command, price, null, type, size, 0, 0);
    }

    /**
     * constructor
     */
    public Software(Software software, String location, double version, double hidden) {
        super(software);

        this.location = location;
        this.version = version;
        this.hidden = hidden;
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
