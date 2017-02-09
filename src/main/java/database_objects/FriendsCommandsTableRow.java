package database_objects;

public class FriendsCommandsTableRow {
    public int id;
    public String owner;
    public String name;
    public String arguments;
    public String access;
    public String location;
    public String username;

    public FriendsCommandsTableRow(int id, String owner, String name, String arguments, String access, String location, String username) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.arguments = arguments;
        this.access = access;
        this.location = location;
        this.username = username;
    }
}
