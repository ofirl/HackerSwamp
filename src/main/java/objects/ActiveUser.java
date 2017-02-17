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
     * gets the defined macros
     * @return the defined macros
     */
    public HashMap<String, String> getMacros() {
        return context.macros;
    }

    /**
     * gets the location of the user
     * @return the location of the user
     */
    public String getLocation() {
        return context.location;
    }

    /**
     * returns whether or not the system is being hacked
     */
    public boolean isBeingHacked() {
        return context.isBeingHacked();
    }

    /**
     * returns whether or not the system is hacked (someone is already in)
     */
    // TODO : change the name from hacked to something more intuitive
    public boolean isHacked() {
        return context.isHacked();
    }

    /**
     * returns whether or not the system is installing new hardware
     */
    public boolean isInstallingHardware() {
        return context.isInstallingHardware();
    }

    /**
     * returns whether or not the system is installing new obstacle
     */
    public boolean isInstallingObstacle() {
        return context.isInstallingObstacle();
    }

    /**
     * gets the system status as arguments to send
     * @return the system status as arguments to send
     */
    public HashMap<String, String> getSystemStatusAsArguments() {
        return context.getSystemStatusAsArguments();
    }

    /**
     * gets the system spec as arguments to send
     * @return the system spec as arguments to send
     */
    public HashMap<String, String> getSystemSpecAsArguments() {
        return context.getSystemSpecAsArguments();
    }

    /**
     * gets the system spec object
     * @return systemSpec object
     */
    public SystemSpec getSystemSpec() {
        return context.getSystemSpec();
    }

    /**
     * gets the main account of the active user
     * @return account object, or null if there isn't one
     */
    public Account getMainAccount() {
        return context.getMainAccount();
    }
}