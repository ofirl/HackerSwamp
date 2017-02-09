package objects;

import java.util.*;

public class ActiveUser {
    // public variables
    public String authKey;
    public String username;
    public int playerId;
    public PlayerContext context;

    /**
     * constructor
     * @param authKey
     * @param username
     * @param playerId
     * @param location
     */
    public ActiveUser(String authKey, String username, int playerId, String location) {
        this.authKey = authKey;
        this.username = username;
        this.playerId = playerId;
        this.context = new PlayerContext(username);
    }

    public HashMap<String, Command> getAvailableCommands() {
        return context.availableCommands;
    }
}
