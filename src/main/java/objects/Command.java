package objects;

import commands.BaseCommand;
import commands.CommandAccess;

import java.util.HashMap;
import java.util.List;

public class Command {
    public int id;
    public String name;
    public Command parent;
    public HashMap<String, Command> subCommands = new HashMap<>();
    public CommandAccess access;
    public List<Argument> arguments;
    public BaseCommand entry;
    public CommandSecurityRating securityRating;

    public Command(int id, String name, BaseCommand entry, CommandAccess access, CommandSecurityRating securityRating) {
        this.id = id;
        this.name = name;
        this.entry = entry;
        this.access = access;
        this.securityRating = securityRating;
    }

    public String execute(CommandContext context, String subCommand, List<Argument> args) {
        return entry.execute(context, subCommand, args);
    }
}
