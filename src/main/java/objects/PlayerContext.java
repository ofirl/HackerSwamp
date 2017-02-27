package objects;

import database_objects.MacrosTableRow;
import database_objects.PlayerCorpsTableRow;
import database_objects.PlayersTableRow;
import items.BaseItem;
import managers.CommandManager;
import database_objects.CommandsViewTableRow;
import interface_objects.DatabaseHandler;
import interface_objects.DatabaseTables;
import managers.DomainsManager;
import managers.ItemManager;
import managers.Logger;

import java.util.*;

/**
 * player context object
 */
public class PlayerContext {
    // public variables
    public String username;
    public int playerId;
    public String corpName;
    public int corpId;
    public String location;
    public HashMap<String, Command> availableCommands = new HashMap<>();
    public HashMap<String, Command> autoCompleteCommands = new HashMap<>();
    public HashMap<String, String> macros = new HashMap<>();
    public SystemSpec systemSpec;
    public SystemStatus systemStatus;
    public Account mainAccount;
    public HashMap<Integer, BaseItem> inventory = new HashMap<>();

    /**
     * constructor
     * @param username the username to create context for
     */
    public PlayerContext(String username) {
        this.username = username;
        this.location = Parameters.DefaultLocation;

        List<PlayersTableRow> player = DatabaseHandler.getTableElements(DatabaseTables.players, null, "username='" + username + "'");
        if (player != null && player.size() > 0) {
            PlayersTableRow playerRow = player.get(0);
            this.playerId = playerRow.id;
            this.corpId = playerRow.corp;
        }

        List<PlayerCorpsTableRow> playerCorp = DatabaseHandler.getTableElements(DatabaseTables.Player_Corps, "corp_name", "username='" + username + "'");
        if (playerCorp != null && playerCorp.size() == 1)
            corpName = playerCorp.get(0).corp_name;

        getAvailableCommands();
        getAutoCompleteCommands();
        getMacros();
        this.systemSpec = SystemSpec.getUserSystemSpecs(username);
        this.mainAccount = DomainsManager.getMainAccountByUsername(username);
        this.inventory = ItemManager.getUserInventory(username);
    }

    /**
     * gets all the available commands of {@code username}
     * @return all the available commands
     */
    public void getAvailableCommands() {
        List<CommandsViewTableRow> accessibleCommands = DatabaseHandler.getTableElements(DatabaseTables.Accessible_Commands, "name", "username='" + username + "'");
        if (accessibleCommands != null) {
            for (CommandsViewTableRow f :
                    accessibleCommands) {
                availableCommands.put(f.name, CommandManager.getCommandByName(f.name));
                Logger.log("PlayerContext.getAvailableCommands", "available command added " + f.name);
            }
        }
    }

    /**
     * gets all the available commands for autocomplete of {@code username}
     * @return all the available commands for auto complete
     */
    public void getAutoCompleteCommands() {
        List<CommandsViewTableRow> accessibleCommands = DatabaseHandler.getTableElements(DatabaseTables.Autocomplete_Commands, "name", "username='" + username + "'");
        if (accessibleCommands != null) {
            for (CommandsViewTableRow f :
                    accessibleCommands)
                autoCompleteCommands.put(f.name, CommandManager.getCommandByName(f.name));
        }
    }

    /**
     * gets all the defined macros for the provided {@code username}
     * @return all the defined macros of {@code username}
     */
    public void getMacros() {
        List<MacrosTableRow> macrosList = DatabaseHandler.getTableElements(DatabaseTables.Macros, null, "player_id=" + playerId);
        for (MacrosTableRow m :
                macrosList)
            macros.put(m.macro, m.command);
    }

    /**
     * changes the location
     * @param location the new location
     */
    public void changeLocation(String location) {
        this.location = location;
    }

    /**
     * returns whether or not the syscmd is being hacked
     */
    public boolean isBeingHacked() {
        return systemStatus.beingHacked;
    }

    /**
     * returns whether or not the syscmd is hacked (someone is already in)
     */
    // TODO : change the name from hacked to something more intuitive
    public boolean isHacked() {
        return systemStatus.hacked;
    }

    /**
     * returns whether or not the syscmd is installing new hardware
     */
    public boolean isInstallingHardware() {
        return systemStatus.installingHardware != 0;
    }

    /**
     * returns whether or not the syscmd is installing new obstacle
     */
    public boolean isInstallingObstacle() {
        return systemStatus.installingObstacle != 0;
    }

    /**
     * gets the syscmd status as arguments to send
     * @return the syscmd status as arguments to send
     */
    public HashMap<String, String> getSystemStatusAsArguments() {
        return systemStatus.getStatusAsArguments();
    }

    /**
     * gets the syscmd specs as arguments to send
     * @return the syscmd spec as arguments to send
     */
    public HashMap<String, String> getSystemSpecAsArguments() {
        return systemSpec.getSpecAsArguments();
    }

    /**
     * get the syscmd spec object
     * @return systemSpec object
     */
    public SystemSpec getSystemSpec() {
        return systemSpec;
    }

    /**
     * gets the main account
     * @return account object, or null if there isn't one
     */
    public Account getMainAccount() {
        return mainAccount;
    }

    /**
     * gets the player inventory
     * @return the player inventory
     */
    public HashMap<Integer, BaseItem> getInventory() {
        return inventory;
    }
}
