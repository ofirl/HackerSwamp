package objects;

import items.*;

import java.util.List;

/**
 * implements system specs
 */
public class SystemSpec {
    // public variables
    public Motherboard motherboard;
    public List<Cpu> cpus;
    public List<Ram> rams;
    public List<Hdd> hdds;
    public NetworkCard networkCard;

    /**
     * constructor
     */
    public SystemSpec(Motherboard motherboard, List<Cpu> cpus, List<Ram> rams, List<Hdd> hdds, NetworkCard networkCard) {
        this.motherboard = motherboard;
        this.cpus = cpus;
        this.rams = rams;
        this.hdds = hdds;
        this.networkCard = networkCard;
    }

    public static SystemSpec getUserSystem(String username) {
        // TODO : implement
        return null;
    }
}
