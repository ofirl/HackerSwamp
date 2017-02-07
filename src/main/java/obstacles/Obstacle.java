package obstacles;

import java.util.HashMap;

public abstract class Obstacle {
    // public variables
    public String name;
    public ObstacleType type;
    public HashMap<String, ObstacleState> playerStates = new HashMap<>();

    public Obstacle(String name, ObstacleType type) {
        this.name = name;
        this.type = type;
        // TODO : pull player states from db (if necessary)
        //this.playerStates = playerStates;
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
        playerStates.put(username, ObstacleState.Removed);
    }

    /**
     * gets the {@link ObstacleState} for the provided {@code username}
     * @param username the username to search for
     * @return the obstacle state
     */
    public ObstacleState getStateForUser(String username) {
        ObstacleState s = playerStates.get(username);
        if (s == null) {
            playerStates.put(username, ObstacleState.Locked);
            s = playerStates.get(username);
        }

        return s;
    }
}
