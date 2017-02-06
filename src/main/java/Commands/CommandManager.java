package Commands;

import objects.*;

import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {

    public static ConcurrentHashMap<String, Command> commandList = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Command> allCommands = new ConcurrentHashMap<>();

    // system commands initializer
    static {
        // help
        addSystemCommand(Parameters.CommandNameHelp, new Help(), true);
        // help.commands
        addSystemCommand(Parameters.CommandNameHelpCommands, new Help(), false);
        // TODO : add implementation for the commands
        // connect
        addSystemCommand(Parameters.CommandNameConnect, null, true);
    }

    /**
     * created and adds a system command to the lists
     * @param name the name of hte ocmmamd
     * @param baseCommand the class that implements the command
     */
    public static void addSystemCommand(String name, BaseCommand baseCommand, boolean mainCommand) {
        Command cmd = new Command(0, name, baseCommand, CommandAccess.System);
        allCommands.put(cmd.name, cmd);
        if (mainCommand)
            commandList.put(cmd.name, cmd);
    }
}
