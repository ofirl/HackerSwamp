package Commands;

import database_objects.CommandsTableRow;
import interface_objects.DatabaseHandler;
import interface_objects.DatabaseTables;
import objects.*;

import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {

    public static ConcurrentHashMap<String, Command> commandList = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Command> allCommands = new ConcurrentHashMap<>();

    /**
     * system commands initializer
     */
    public static void init() {
        // help
        addInitCommands(Parameters.CommandNameHelp, new Help(), true);
        // help.commands
        addInitCommands(Parameters.CommandNameHelpCommands, new Help(), false);
        // connect
        addInitCommands(Parameters.CommandNameConnect, new Connect(), true);
        // TODO : add implementation for the commands
    }

    /**
     * used to add commands during init
     * @param name the name of the command
     * @param baseCommand the {@code baseCommand} of the commnad
     * @param mainCommand whether this command is a main command
     */
    public static void addInitCommands(String name, BaseCommand baseCommand, boolean mainCommand) {
        String filter = "owner='system' AND name='" + name + "'";
        CommandsTableRow row = DatabaseHandler.<CommandsTableRow>getTableElements(DatabaseTables.Commands, null, filter).get(0);
        addSystemCommand(row.id, name, baseCommand, mainCommand);
    }

    /**
     * created and adds a system command to the lists
     * @param name the name of hte ocmmamd
     * @param baseCommand the class that implements the command
     */
    public static void addSystemCommand(int id, String name, BaseCommand baseCommand, boolean mainCommand) {
        addCommand(id, name, baseCommand, mainCommand, CommandAccess.System);
    }

    /**
     * created and adds a system command to the lists
     * @param name the name of hte ocmmamd
     * @param baseCommand the class that implements the command
     * @param access the command access type
     */
    public static void addCommand(int id, String name, BaseCommand baseCommand, boolean mainCommand, CommandAccess access) {
        Command cmd = new Command(id, name, baseCommand, access);
        allCommands.put(cmd.name, cmd);
        if (mainCommand)
            commandList.put(cmd.name, cmd);
    }

    /**
     * gets the command with the provided {@code name}
     * @param name the name to search for
     * @return the command or null if not found
     */
    public static Command getCommandByName(String name) {
        return allCommands.get(name);
    }
}
