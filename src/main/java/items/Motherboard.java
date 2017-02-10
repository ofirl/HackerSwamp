package items;

/**
 * implements motherboard item
 */
public class Motherboard extends BaseItem{
    // public variables
    public int cpuSlots;
    public int ramSlots;
    public int hddSlots;

    /**
     * constructor
     */
    public Motherboard(int id, String name, int cpuSlots, int ramSlots, int hddSlots) {
        super(id, name);
        this.cpuSlots = cpuSlots;
        this.ramSlots = ramSlots;
        this.hddSlots = hddSlots;
    }
}
