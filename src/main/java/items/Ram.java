package items;

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
}
