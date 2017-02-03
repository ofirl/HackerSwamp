package objects;

import Commands.BaseCommand;

import java.util.HashMap;
import java.util.List;

public class Command {
    public int id;
    public String name;
    public Command parent;
    public HashMap<String, Command> subCommands;
    public CommandAccess access;
    public List<Argument> arguments;
    public BaseCommand entry;

    public Command(int id, String name, BaseCommand entry, CommandAccess access) {
        this.id = id;
        this.name = name;
        this.entry = entry;
        this.access = access;
    }
}
