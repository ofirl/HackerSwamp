package objects;

public class CommandContext {
    public String username;
    public int playerId;
    public String location;

    public CommandContext(String username, int playerId, String location) {
        this.username = username;
        this.playerId = playerId;
        this.location = location;
    }
}
