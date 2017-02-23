package items;

import java.util.HashMap;

public class Ram extends BaseItem{
    // public variables
    public int size;

    /**
     * constructor
     */
    public Ram(int id, String name, int size) {
        super(id, name);
        this.size = size;
    }

    /**
     * gets the ram as arguments to send
     * @return the ram as arguments
     */
    public HashMap<String, String> getSpecAsArguments() {
        HashMap<String, String> ram = new HashMap<>();

        ram.put("ram_id", String.valueOf(id));
        ram.put("ram_name", name);
        ram.put("ram_size", String.valueOf(size));

        return ram;
    }

    /**
     * detailed info about the item
     */
    @Override
    public String toString() {
        String output = "";

        output += super.toString() + "\n";
        output += "Size : " + size + " Mb";

        return output;
    }
}
