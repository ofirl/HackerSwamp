package objects;

import database_objects.MacrosTableRow;
import database_objects.PlayersTableRow;
import managers.CommandManager;
import database_objects.CommandsViewTableRow;
import interface_objects.DatabaseHandler;
import interface_objects.DatabaseTables;

import java.util.*;

public class PlayerContext {
    // public variables
    public String username;
    public int playerId;
    // TODO : get corp name on object creation
    public String corpName;
    public int corpId;
    public String location;
    public HashMap<String, Command> availableCommands;
    public HashMap<String, Command> autoCompleteCommands;
    public HashMap<String, String> macros;
    public SystemSpec systemSpec;
    public SystemStatus systemStatus;

    /**
     * constructor
     * @param username
     */
    public PlayerContext(String username) {
        this.username = username;
        this.location = Parameters.DefaultLocation;

        List<PlayersTableRow> player = DatabaseHandler.getTableElements(DatabaseTables.Players, null, "username='" + username + "'");
        if (player != null && player.size() > 0) {
            PlayersTableRow playerRow = player.get(0);
            this.playerId = playerRow.id;
            this.corpId = playerRow.corp;
        }

        this.availableCommands = getAvailableCommands(username);
        this.autoCompleteCommands = getAutoCompleteCommands(username);
        this.macros = getMacros(username);
        this.systemSpec = SystemSpec.getUserSystemSpecs(username);
    }

    /**
     * gets all the available commands of {@code username}
     * @param username the username to filter for
     * @return all the available commands
     */
    public HashMap<String, Command> getAvailableCommands(String username) {
        HashMap<String, Command> commands = new HashMap<>();

        List<CommandsViewTableRow> accessibleCommands = DatabaseHandler.getTableElements(DatabaseTables.Accessible_Commands, "name", "username='" + username + "'");
        if (accessibleCommands != null) {
            for (CommandsViewTableRow f :
                    accessibleCommands)
                availableCommands.put(f.name, CommandManager.getCommandByName(f.name));
        }

        return commands;
    }

    /**
     * gets all the available commands for autocomplete of {@code username}
     * @param username the username to filter for
     * @return all the available commands for auto complete
     */
    public HashMap<String, Command> getAutoCompleteCommands(String username) {
        HashMap<String, Command> commands = new HashMap<>();

        List<CommandsViewTableRow> accessibleCommands = DatabaseHandler.getTableElements(DatabaseTables.Autocomplete_Commands, "name", "username='" + username + "'");
        if (accessibleCommands != null) {
            for (CommandsViewTableRow f :
                    accessibleCommands)
                availableCommands.put(f.name, CommandManager.getCommandByName(f.name));
        }

        return commands;
    }

    /**
     * gets all the defined macros for the provided {@code username}
     * @param username the username to search for
     * @return all the defined macros of {@code username}
     */
    public HashMap<String, String> getMacros(String username) {
        HashMap<String, String> macros = new HashMap<>();
        List<MacrosTableRow> macrosList = DatabaseHandler.getTableElements(DatabaseTables.Macros, null, "player_id=" + playerId);
        for (MacrosTableRow m :
                macrosList)
            macros.put(m.macro, m.command);

        return macros;
    }

    /**
     * changes the location
     * @param location the new location
     */
    public void changeLocation(String location) {
        this.location = location;
    }

    /**
     * returns whether or not the system is being hacked
     */
    public boolean isBeingHacked() {
        return systemStatus.beingHacked;
    }

    /**
     * returns whether or not the system is hacked (someone is already in)
     */
    // TODO : change the name from hacked to something more intuitive
    public boolean isHacked() {
        return systemStatus.hacked;
    }

    /**
     * returns whether or not the system is installing new hardware
     */
    public boolean isInstallingHardware() {
        return systemStatus.installingHardware != 0;
    }

    /**
     * returns whether or not the system is installing new obstacle
     */
    public boolean isInstallingObstacle() {
        return systemStatus.installingObstacle != 0;
    }
}
