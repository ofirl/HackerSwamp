package items;

public class Cpu extends BaseItem{
    // public variables
    public int speed;

    /**
     * constructor
     */
    public Cpu(int id, String name, int speed) {
        super(id, name);
        this.speed = speed;
    }
}
