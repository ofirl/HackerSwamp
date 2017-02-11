package items;

import java.util.HashMap;

public class Hdd extends BaseItem{
    // public variables
    public int size;

    /**
     * constructor
     */
    public Hdd(int id, String name, int size) {
        super(id, name);
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
}
