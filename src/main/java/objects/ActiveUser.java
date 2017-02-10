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
     * @param authKey authentication key
     * @param username username
     * @param playerId player id (as in the database table)
     */
    public ActiveUser(String authKey, String username, int playerId) {
        this.authKey = authKey;
        this.username = username;
        this.playerId = playerId;
        this.context = new PlayerContext(username);
    }

    /**
     * get the available commands for the user
     * @return all the available commands
     */
    public HashMap<String, Command> getAvailableCommands() {
        return context.availableCommands;
    }

    /**
     * get the available commands for auto complete the user
     * @return all the available commands for auto complete
     */
    public HashMap<String, Command> getAutoCompleteCommands() {
        return context.autoCompleteCommands;
    }

    /**
     * gets the location of the user
     * @return the location of the user
     */
    public String getLocation() {
        return context.location;
    }
}
