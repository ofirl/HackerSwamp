package objects;

import commands.CommandManager;
import database_objects.CommandsViewTableRow;
import interface_objects.DatabaseHandler;
import interface_objects.DatabaseTables;

import java.util.*;

public class PlayerContext {
    // public variables
    public String username;
    public String location;
    public HashMap<String, Command> availableCommands;
    public HashMap<String, Command> autoCompleteCommands;

    /**
     * constructor
     * @param username
     */
    public PlayerContext(String username) {
        this.username = username;
        this.location = Parameters.DefaultLocation;
        this.availableCommands = getAvailableCommands(username);
        this.autoCompleteCommands = getAutoCompleteCommands(username);
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
     * changes the location
     * @param location the new location
     */
    public void changeLocation(String location) {
        this.location = location;
    }
}
