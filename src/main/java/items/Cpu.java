package items;

import java.util.HashMap;

public class Cpu extends BaseItem{
    // public variables
    public double speed;
    public int cores;

    /**
     * constructor
     */
    public Cpu(int id, String name, double speed, int cores) {
        super(id, name);
        this.speed = speed;
        this.cores = cores;
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
        cpu.put("cpu_cores", String.valueOf(cores));

        return cpu;
    }

    /**
     * detailed info about the item
     */
    @Override
    public String toString() {
        String output = "";

        output += super.toString() + "\n";
        output += "Speed : " + speed + " Ghz\n";
        output += "# of cores : " + cores;

        return output;
    }
}
