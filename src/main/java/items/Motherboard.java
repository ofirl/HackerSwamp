package items;

import java.util.HashMap;

/**
 * implements motherboard item
 */
public class Motherboard extends BaseItem{
    // public variables
    public int cpuSlots;
    public int ramSlots;
    public int hddSlots;
    public int maxRamSize;
    public double maxCpuSpeed;

    /**
     * constructor
     */
    public Motherboard(int id, String name, int cpuSlots, int ramSlots, int hddSlots, int maxRamSize, double maxCpuSpeed) {
        super(id, name);
        this.cpuSlots = cpuSlots;
        this.ramSlots = ramSlots;
        this.hddSlots = hddSlots;
        this.maxRamSize = maxRamSize;
        this.maxCpuSpeed = maxCpuSpeed;
    }

    /**
     * gets the mother board as arguments to send
     * @return the motherboard as arguments
     */
    public HashMap<String, String> getSpecAsArguments() {
        HashMap<String, String> motherboard = new HashMap<>();

        motherboard.put("motherboard_id", String.valueOf(id));
        motherboard.put("motherboard_name", name);
        motherboard.put("motherboard_cpu_slots", String.valueOf(cpuSlots));
        motherboard.put("motherboard_ram_slots", String.valueOf(ramSlots));
        motherboard.put("motherboard_hdd_slots", String.valueOf(hddSlots));
        motherboard.put("motherboard_max_ram_size", String.valueOf(maxRamSize));
        motherboard.put("motherboard_max_cpu_speed", String.valueOf(maxCpuSpeed));

        return motherboard;
    }
}
