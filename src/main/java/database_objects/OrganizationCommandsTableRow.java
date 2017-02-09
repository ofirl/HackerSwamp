package database_objects;

public class OrganizationCommandsTableRow {
    public int id;
    public String owner;
    public String name;
    public String arguments;
    public String access;
    public String location;
    public int organization;

    public OrganizationCommandsTableRow(int id, String owner, String name, String arguments, String access, String location, int organization) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.arguments = arguments;
        this.access = access;
        this.location = location;
        this.organization = organization;
    }
}
