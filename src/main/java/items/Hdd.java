package items;

import java.util.HashMap;

public class Hdd extends BaseItem{
    // public variables
    public int size;

    /**
     * constructor
     */
    public Hdd(int id, String name, int price, int size) {
        super(id, name, price);
        this.size = size;
    }

    /**
     * gets the hdd as arguments to send
     * @return the hdd as arguments
     */
    public HashMap<String, String> getSpecAsArguments() {
        HashMap<String, String> hdd = new HashMap<>();

        hdd.put("hdd_id", String.valueOf(id));
        hdd.put("hdd_name", name);
        hdd.put("hdd_size", String.valueOf(size));

        return hdd;
    }

    /**
     * detailed info about the item
     */
    @Override
    public String toString() {
        String output = "";

        output += super.toString() + "\n";

        if (size >= 1024)
            output += "Size : " + size / 1024 + " Tb\n";
        else if (size >= 1)
            output += "Size : " + size + " Gb\n";
        else
            output += "Size : " + size * 1024 + " Mb\n";

        return output;
    }
}
