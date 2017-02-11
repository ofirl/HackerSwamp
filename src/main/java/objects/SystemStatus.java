package objects;

import java.util.Date;
import java.util.HashMap;

/**
 * object for system status management
 */
public class SystemStatus {
    // public variables
    public boolean beingHacked;
    public boolean hacked;

    public int installingObstacle;
    public Date obstacleInstallFinish;

    public int installingHardware;
    public Date hardwareInstallFinish;

    /**
     * constructor
     */
    public SystemStatus() {
        // TODO : check status and initialize accordingly
    }

    // TODO : add methods to get what is installing

    // TODO : add methods to get more detailed status of the current hack

    /**
     * gets the system status as arguments
     * @return the system status as arguments
     */
    public HashMap<String, String> getStatusAsArguments() {
        HashMap<String, String> systemStatus = new HashMap<>();

        if (beingHacked)
            systemStatus.put("beingHacked", "true");

        if (hacked)
            systemStatus.put("hacked", "true");

        // TODO : add details about when installation ends
        if (installingObstacle != 0)
            systemStatus.put("installingObstacle", "true");

        // TODO : add details about when installation ends
        if (installingHardware != 0)
            systemStatus.put("installingHardware", "true");

        return systemStatus;
    }
}
