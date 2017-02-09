package objects;

import Commands.CommandManager;
import database_objects.FriendsCommandsTableRow;
import database_objects.OrganizationCommandsTableRow;
import database_objects.PlayersTableRow;
import interface_objects.DatabaseHandler;
import interface_objects.DatabaseTables;

import java.util.*;

public class PlayerContext {
    // public variables
    public String username;
    public String location;
    public HashMap<String, Command> availableCommands;

    /**
     * constructor
     * @param username
     */
    public PlayerContext(String username) {
        this.username = username;
        this.location = "localhost";
        this.availableCommands = getAvailableCommands(username);
    }

    /**
     * gets all the available commands of {@code username}
     * @param username the username to filter for
     * @return all the available commands
     */
    public HashMap<String, Command> getAvailableCommands(String username) {
        HashMap<String, Command> commands = new HashMap<>();

        List<FriendsCommandsTableRow> friendsCommands = DatabaseHandler.getTableElements(DatabaseTables.Friends_Commands, "name", "username='" + username + "'");
        if (friendsCommands != null) {
            for (FriendsCommandsTableRow f :
                    friendsCommands)
                availableCommands.put(f.name, CommandManager.getCommandByName(f.name));
        }

        List<PlayersTableRow> playerCorps = DatabaseHandler.getTableElements(DatabaseTables.Players, "corp", "username='" + username + "'");
        int corp = 0;
        // TODO : check that 0 is correct here (default value and not null), if so the last condition is redundant - just assign the corp variable...
        if (playerCorps != null && playerCorps.size() != 1 && playerCorps.get(0).corp != 0)
            corp = playerCorps.get(0).corp;

        List<OrganizationCommandsTableRow> organizationCommands = DatabaseHandler.getTableElements(DatabaseTables.Organization_Commands, "name", "organization=" + corp);
        if (organizationCommands != null) {
            for (OrganizationCommandsTableRow o :
                    organizationCommands)
                availableCommands.put(o.name, CommandManager.getCommandByName(o.name));
        }

        return commands;
    }

    public void changeLocation(String location) {
        this.location = location;
    }
}
