package items;

import objects.Command;

import java.util.HashMap;

public class MarketScript extends BaseItem{
    // public variables
    public Command command;
    public int price;
    public String owner;

    /**
     * constructor
     * @param id id of the item
     * @param name name of the item
     * @param command command of the {@code MarketScript}
     * @param price price of the script
     * @param owner owner of the script
     */
    public MarketScript(int id, String name, Command command, int price, String owner) {
        super(id, name);
        this.command = command;
        this.price = price;
        this.owner = owner;
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
        output += "Owner : " + owner;

        return output;
    }
}
