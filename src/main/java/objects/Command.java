package objects;

import java.util.HashMap;
import java.util.List;

public class Command {
    public int id;
    public String name;
    public Command parent;
    public HashMap<String, Command> subCommands;
    public List<Argument> arguments;

    public Command(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
