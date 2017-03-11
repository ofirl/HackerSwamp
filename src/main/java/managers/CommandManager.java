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
    public static ConcurrentHashMap<String, List<Command>> allCommands = new ConcurrentHashMap<>();

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
        String filter = "security_rating='syscmd'";
        List<CommandsTableRow> rows = DatabaseHandler.getTableElements(DatabaseTables.Commands, "id, name, owner", filter);

        // sanity check
        //TODO : throw error
        if (rows == null)
            return;

        HashMap<String, Integer> commandIds = new HashMap<>();

        // parse syscmd commands
        for (CommandsTableRow c :
                rows)
            commandIds.put(c.owner + "." + c.name, c.id);

        String name, subName;
        Command cmd, subCmd;

        // region system commands

        // help
        name = "systemUser";
        subName = Parameters.CommandNameHelp;
        cmd = addSystemCommand(commandIds.get(name + "." + subName), subName, new Help(), true);

        // help.commands
        name = Parameters.CommandNameHelp;
        subName = Parameters.CommandNameHelpCommands;
        subCmd = addSystemCommand(commandIds.get(name + "." + subName), subName, new Help(), false);
        addSubCommand(cmd, subCmd);

        // connect
        name = "systemUser";
        subName = Parameters.CommandNameConnect;
        cmd = addSystemCommand(commandIds.get(name + "." + subName), subName, new Connect(), true);

        // syscmd
        name = "systemUser";
        subName = Parameters.CommandNameSystem;
        cmd = addSystemCommand(commandIds.get(name + "." + subName), subName, new System(), true);

        // syscmd.spec
        name = Parameters.CommandNameSystem;
        subName = Parameters.CommandNameSystemSpec;
        subCmd = addSystemCommand(commandIds.get(name + "." + subName), subName, new System(), false);
        addSubCommand(cmd, subCmd);

        // market
        name = "systemUser";
        subName = Parameters.CommandNameMarket;
        cmd = addSystemCommand(commandIds.get(name + "." + subName), subName, new Market(), true);

        // market.items
        name = Parameters.CommandNameMarket;
        subName = Parameters.CommandNameMarketItems;
        subCmd = addSystemCommand(commandIds.get(name + "." + subName), subName, new Market(), false);
        addSubCommand(cmd, subCmd);

        // market.scripts
        name = Parameters.CommandNameMarket;
        subName = Parameters.CommandNameMarketScripts;
        subCmd = addSystemCommand(commandIds.get(name + "." + subName), subName, new Market(), false);
        addSubCommand(cmd, subCmd);

        // macro
        name = "systemUser";
        subName = Parameters.CommandNameMacro;
        cmd = addSystemCommand(commandIds.get(name + "." + subName), subName, new Macro(), true);

        // macro.add
        name = Parameters.CommandNameMacro;
        subName = Parameters.CommandNameMacroAdd;
        subCmd = addSystemCommand(commandIds.get(name + "." + subName), subName, new Macro(), false);
        addSubCommand(cmd, subCmd);

        // macro.remove
        name = Parameters.CommandNameMacro;
        subName = Parameters.CommandNameMacroRemove;
        subCmd = addSystemCommand(commandIds.get(name + "." + subName), subName, new Macro(), false);
        addSubCommand(cmd, subCmd);

        // macro.view
        name = Parameters.CommandNameMacro;
        subName = Parameters.CommandNameMacroView;
        subCmd = addSystemCommand(commandIds.get(name + "." + subName), subName, new Macro(), false);
        addSubCommand(cmd, subCmd);

        // disconnect
        name = Parameters.CommandNameSystemUser;
        subName = Parameters.CommandNameDisconnect;
        cmd = addSystemCommand(commandIds.get(name + "." + subName), subName, new Disconnect(), true);

        // dc
        name = Parameters.CommandNameSystemUser;
        subName = Parameters.CommandNameDc;
        cmd = addSystemCommand(commandIds.get(name + "." + subName), subName, new Disconnect(), true);

        // collect
        name = Parameters.CommandNameSystemUser;
        subName = Parameters.CommandNameCollect;
        cmd = addSystemCommand(commandIds.get(name + "." + subName), subName, new Collect(), true);

        // endregion

        // region software commands

        // bcminer
        name = Parameters.CommandNameSystemUser;
        subName = Parameters.CommandNameBcMiner;
        cmd = addSoftwareCommand(commandIds.get(name + "." + subName), subName, new BcMiner(), false);

        // endregion

        // region location commands

        // logs
        name = "systemUser";
        subName = Parameters.CommandNameLogs;
        cmd = addSystemCommand(commandIds.get(name + "." + subName), subName, new Logs(), false);

        // logs.view
        name = Parameters.CommandNameLogs;
        subName = Parameters.CommandNameLogsView;
        subCmd = addSystemCommand(commandIds.get(name + "." + subName), subName, new Logs(), false);
        addSubCommand(cmd, subCmd);

        // logs.delete
        name = Parameters.CommandNameLogs;
        subName = Parameters.CommandNameLogsDelete;
        subCmd = addSystemCommand(commandIds.get(name + "." + subName), subName, new Logs(), false);
        addSubCommand(cmd, subCmd);

        // loot
        name = "systemUser";
        subName = Parameters.CommandNameLoot;
        cmd = addSystemCommand(commandIds.get(name + "." + subName), subName, new Loot(), false);

        // install
        name = "systemUser";
        subName = Parameters.CommandNameInstall;
        cmd = addSystemCommand(commandIds.get(name + "." + subName), subName, new Install(), false);

        // uninstall
        name = "systemUser";
        subName = Parameters.CommandNameUninstall;
        cmd = addSystemCommand(commandIds.get(name + "." + subName), subName, new Uninstall(), false);

        // copy
        name = "systemUser";
        subName = Parameters.CommandNameCopy;
        cmd = addSystemCommand(commandIds.get(name + "." + subName), subName, new Copy(), false);

        // delete
        name = "systemUser";
        subName = Parameters.CommandNameDelete;
        cmd = addSystemCommand(commandIds.get(name + "." + subName), subName, new Delete(), false);

        // endregion
    }

    /**
     * init for player scripts
     */
    public static void initPlayerScripts(){
        // get command list from db
        String filter = "access!='" + CommandAccess.System + "' AND access!='Location' AND (owner='" + Parameters.CommandNameSystemUser + "' AND access!='Private')";
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
            putCommand(c.name, command);
            if (!c.owner.equals("systemUser"))
                parents.put(command, c.owner);
        }

        // add commands to their parents sub commands list
        for (Command c :
                parents.keySet()) {
            addSubCommand(getCommandByName(parents.get(c)), c);
        }
    }

    /**
     * adds a command to allCommands
     */
    public static void putCommand(String key, Command value) {
        if (!allCommands.containsKey(key))
            allCommands.put(key, new ArrayList<>());

        allCommands.get(key).add(value);
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
     * created and adds a software command (used by scripts and software) to the lists
     * @param name the name of hte command
     * @param baseCommand the class that implements the command
     */
    public static Command addSoftwareCommand(int id, String name, BaseCommand baseCommand, boolean mainCommand) {
        return addCommand(id, name, baseCommand, mainCommand, CommandAccess.Private, CommandSecurityRating.syscmd);
    }

    /**
     * created and adds a syscmd command to the lists
     * @param name the name of hte command
     * @param baseCommand the class that implements the command
     * @param access the command access type
     */
    public static Command addCommand(int id, String name, BaseCommand baseCommand, boolean mainCommand, CommandAccess access, CommandSecurityRating securityRating) {
        Command cmd = new Command(id, name, baseCommand, access, securityRating);
        putCommand(cmd.name, cmd);
        if (mainCommand)
            commandList.put(cmd.name, cmd);

        return cmd;
    }

    /**
     * gets the command with the provided {@code name},
     * returns the first command if there is more than one command in the list
     * @param name the name to search for
     * @return the command or null if not found
     */
    public static Command getCommandByName(String name) {
        List<Command> commandList = allCommands.get(name);
        if (commandList != null)
            return commandList.get(0);

        return null;
    }

    /**
     * gets the command with the provided {@code id}
     * @param id the id to search for
     * @return the command or null if not found
     */
    public static Command getCommandById(int id) {
        for (List<Command> c :
                allCommands.values()) {
            for (Command com :
                    c) {
                if (com.id == id)
                    return com;
            }
        }

        return null;
    }
}
