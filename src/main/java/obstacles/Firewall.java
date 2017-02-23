package obstacles;

public class Firewall extends Obstacle{
    // public variables
    public int strength;

    /**
     * constructor
     * @param name the name of the firewall
     * @param strength the strength of the firewall
     */
    public Firewall(int id, String name, int strength) {
        super(id, name, ObstacleType.Firewall);
        this.strength = strength;
    }

    /**
     * encounters the firewall
     * @return message or null if successfully removed
     */
    public String encounter() {
        // TODO : implement
        return null;
    }
}
