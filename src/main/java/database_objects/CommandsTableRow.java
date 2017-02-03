package database_objects;

public class CommandsTableRow {
    public int id;
    public String owner;
    public String name;
    public String arguments;
    public String access;

    public String[] args;

    public void parseArguments() {
        if (arguments != null)
            args = arguments.split(",");
    }
}
