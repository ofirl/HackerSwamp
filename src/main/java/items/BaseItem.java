package items;

import java.util.HashMap;

public abstract class BaseItem {
    // public variables
    public int id;
    public String name;
    public int price;

    /**
     * constructor
     * @param id id of the item
     * @param name name of the item
     */
    public BaseItem(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    /**
     * gets the item as arguments to send
     * @return the item as arguments
     */
    public abstract HashMap<String, String> getSpecAsArguments();

    /**
     * detailed info about the item
     */
    @Override
    public String toString() {
        return "Name : " + name;
    }
}
