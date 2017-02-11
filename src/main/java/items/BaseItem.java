package items;

import java.util.HashMap;

public abstract class BaseItem {
    // public variables
    public int id;
    public String name;

    public BaseItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public abstract HashMap<String, String> getSpecAsArguments();
}
