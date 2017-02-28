package managers;

import commands.*;
import commands.System;
import database_objects.CommandsTableRow;
import interface_objects.DatabaseHandler;
import interface_objects.DatabaseTables;
import objects.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * manager class for commands
 */
public class CommandManager {
    // public variables
    public static ConcurrentHashMap<String, Command> commandList = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Command> allCommands = new ConcurrentHashMap<>();

    /**
     * commands initializer
     */
    public static void init() {
        initSystemCommands();
        initPlayerScripts();
    }

    /**
     * init for syscmd commands
     */
    public static void initSystemCommands() {
        // get command list from db
        String filter = "access='" + CommandAccess.System + "'";
        List<CommandsTableRow> rows = DatabaseHandler.getTableElements(DatabaseTables.Commands, "id, name", filter);

        // sanity check
        //TODO : throw error
        if (rows == null)
            return;

        HashMap<String, Integer> commandIds = new HashMap<>();

        // parse syscmd commands
        for (CommandsTableRow c :
                rows)
            commandIds.put(c.name, c.id);

        String name;
        Command cmd, subCmd;

        // help
        name = Parameters.CommandNameHelp;
        cmd = addSystemCommand(commandIds.get(name), name, new Help(), true);
        // help.commands
        name = Parameters.CommandNameHelpCommands;
        subCmd = addSystemCommand(commandIds.get(name), name, new Help(), false);
        addSubCommand(cmd, subCmd);
        // connect
        name = Parameters.CommandNameConnect;
        cmd = addSystemCommand(commandIds.get(name), name, new Connect(), true);
        // syscmd
        name = Parameters.CommandNameSystem;
        cmd = addSystemCommand(commandIds.get(name), name, new System(), true);
        // syscmd.spec
        name = Parameters.CommandNameSystemSpec;
        subCmd = addSystemCommand(commandIds.get(name), name, new System(), false);
        addSubCommand(cmd, subCmd);
        // market
        name = Parameters.CommandNameMarket;
        cmd = addSystemCommand(commandIds.get(name), name, new Market(), true);
        // market.items
        name = Parameters.CommandNameMarketItems;
        subCmd = addSystemCommand(commandIds.get(name), name, new Market(), false);
        addSubCommand(cmd, subCmd);
        // market.scripts
        name = Parameters.CommandNameMarketScripts;
        subCmd = addSystemCommand(commandIds.get(name), name, new Market(), false);
        addSubCommand(cmd, subCmd);
        // macro
        name = Parameters.CommandNameMacro;
        cmd = addSystemCommand(commandIds.get(name), name, new Macro(), true);
        // macro.add
        name = Parameters.CommandNameMacroAdd;
        subCmd = addSystemCommand(commandIds.get(name), name, new Macro(), false);
        addSubCommand(cmd, subCmd);
        // macro.remove
        name = Parameters.CommandNameMacroRemove;
        subCmd = addSystemCommand(commandIds.get(name), name, new Macro(), false);
        addSubCommand(cmd, subCmd);
        // macro.view
        name = Parameters.CommandNameMacroView;
        subCmd = addSystemCommand(commandIds.get(name), name, new Macro(), false);
        addSubCommand(cmd, subCmd);
        // TODO : add implementation for the commands
    }

    /**
     * init for player scripts
     */
    public static void initPlayerScripts(){
        // get command list from db
        String filter = "access!='" + CommandAccess.System + "'";
        List<CommandsTableRow> dbCommands = DatabaseHandler.getTableElements(DatabaseTables.Commands, null, filter);
        if (dbCommands == null)
            return;

        HashMap<Command, String> parents = new HashMap<>();

        // add command to commandList (if needed) and allCommands
        for (CommandsTableRow c :
                dbCommands) {
            // TODO : get the correct callable instead of null
            Command command = new Command(c.id, c.name, null, CommandAccess.valueOf(c.access), CommandSecurityRating.valueOf(c.security_rating));

            // parse arguments
            if (c.arguments != null && !c.arguments.equals("")) {
                String[] argsArray = c.arguments.split(",");
                List<Argument> argsList = new ArrayList<>();
                for (String a :
                        argsArray) {
                    String[] aParts = a.split(":");
                    argsList.add(new Argument(aParts[0], aParts[1]));
                }
                command.arguments = argsList;
            }

            // add to commandsList and allCommands
            allCommands.put(c.name, command);
            if (!c.owner.equals("systemUser"))
                parents.put(command, c.owner);
        }

        // add commands to their parents sub commands list
        for (Command c :
                parents.keySet()) {
            addSubCommand(allCommands.get(parents.get(c)), c);
        }
    }

    /**
     * adds a {@code sub} as a sub command to {@code main}
     * @param main the main command
     * @param sub the sub command
     */
    public static void addSubCommand(Command main, Command sub) {
        Logger.log("DatabaseHandler.addSubCommand", sub.name);
        Logger.log("DatabaseHandler.addSubCommand", main.name);
        main.subCommands.put(sub.name, sub);
        sub.parent = main;
    }

    /**
     * created and adds a syscmd command to the lists
     * @param name the name of hte command
     * @param baseCommand the class that implements the command
     */
    public static Command addSystemCommand(int id, String name, BaseCommand baseCommand, boolean mainCommand) {
        return addCommand(id, name, baseCommand, mainCommand, CommandAccess.System, CommandSecurityRating.syscmd);
    }

    /**
     * created and adds a syscmd command to the lists
     * @param name the name of hte command
     * @param baseCommand the class that implements the command
     * @param access the command access type
     */
    public static Command addCommand(int id, String name, BaseCommand baseCommand, boolean mainCommand, CommandAccess access, CommandSecurityRating securityRating) {
        Command cmd = new Command(id, name, baseCommand, access, securityRating);
        allCommands.put(cmd.name, cmd);
        if (mainCommand)
            commandList.put(cmd.name, cmd);

        return cmd;
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
