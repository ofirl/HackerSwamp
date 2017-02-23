package obstacles;

import database_objects.DisabledObstaclesUsersTableRow;
import interface_objects.DatabaseHandler;
import interface_objects.DatabaseTables;

import java.util.*;

public abstract class Obstacle {
    // public variables
    public int id;
    public String name;
    public ObstacleType type;
    public HashMap<String, ObstacleState> playerStates = new HashMap<>();

    public Obstacle(int id, String name, ObstacleType type) {
        this.id = id;
        this.name = name;
        this.type = type;

        // pull player states from db
        List<DisabledObstaclesUsersTableRow> disabledUsers = DatabaseHandler.getTableElements(DatabaseTables.Disabled_Obstacles_Users, null, "obstacle=" + id);
        if (disabledUsers == null) {
            // TODO : add error log
            return;
        }

        for (DisabledObstaclesUsersTableRow d :
                disabledUsers)
            playerStates.put(d.username, ObstacleState.Disabled);
    }

    /**
     * encounter the obstacle
     * @return a message
     */
    public abstract String encounter();

    /**
     * removes the obstacle for the specified {@code username}
     * @param username the username for which to remove the obstacle
     */
    public void remove(String username) {
        playerStates.put(username, ObstacleState.Disabled);
    }

    /**
     * gets the {@link ObstacleState} for the provided {@code username}
     * @param username the username to search for
     * @return the obstacle state
     */
    public ObstacleState getStateForUser(String username) {
        ObstacleState s = playerStates.get(username);
        if (s == null) {
            playerStates.put(username, ObstacleState.Enabled);
            s = playerStates.get(username);
        }

        return s;
    }
}
