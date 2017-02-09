package objects;

import java.util.*;

public class ActiveUser {
    // public variables
    public final String authKey;
    public final String username;
    public final int playerId;
    public final PlayerContext context;

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

    public String getLocation() {
        return context.location;
    }
}
