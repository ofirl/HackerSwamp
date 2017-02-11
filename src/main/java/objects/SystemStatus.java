package objects;

import java.util.Date;

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

    // TODO : add methods to get what is installing

    // TODO : add methods to get more detailed status of the current hack
}
