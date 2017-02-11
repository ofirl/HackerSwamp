package items;

import java.util.HashMap;

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

    /**
     * gets the cpu as arguments to send
     * @return the cpu as arguments
     */
    public HashMap<String, String> getSpecAsArguments() {
        HashMap<String, String> cpu = new HashMap<>();

        cpu.put("cpu_id", String.valueOf(id));
        cpu.put("cpu_name", name);
        cpu.put("cpu_speed", String.valueOf(speed));

        return cpu;
    }
}
