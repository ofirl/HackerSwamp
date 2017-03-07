package items;

import objects.Command;

import java.util.HashMap;

public class MarketScript extends BaseItem{
    // public variables
    public Command command;
    public String creator;
    public MarketScriptType type;

    /**
     * constructor
     * @param id id of the item
     * @param name name of the item
     * @param command command of the {@code MarketScript}
     * @param price price of the script
     * @param creator creator of the script
     * @param type type of the script
     */
    public MarketScript(int id, String name, Command command, int price, String creator, MarketScriptType type) {
        super(id, name, price);
        this.command = command;
        this.price = price;
        this.creator = creator;
        this.type = type;
    }

    public MarketScript(MarketScript marketScript) {
        this(marketScript.id, marketScript.name, marketScript.command, marketScript.price, marketScript.creator, marketScript.type);
    }

    /**
     * gets the market script as arguments to send
     * @return the market script as arguments
     */
    @Override
    public HashMap<String, String> getSpecAsArguments() {
        HashMap<String, String> cmd = new HashMap<>();

        cmd.put("market_script_id", String.valueOf(id));
        cmd.put("market_script_name", name);
        cmd.put("market_script_command_id", String.valueOf(command.id));
        cmd.put("market_script_command_name", command.name);
        cmd.put("market_script_type", type.name());

        return cmd;
    }

    /**
     * detailed info about the item
     */
    @Override
    public String toString() {
        String output = "";

        output += super.toString() + "\n";
        // TODO : add units
        output += "Price : " + price + " <units> per use\n";
        output += "Owner : " + creator + "\n";
        output += "Type : " + type;

        return output;
    }
}
