package items;

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
}
