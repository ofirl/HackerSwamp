package objects;

public class ActiveUser {
    public String authKey;
    public String username;
    public int playerId;
    public String location;

    public ActiveUser(String authKey, String username, int playerId, String location) {
        this.authKey = authKey;
        this.username = username;
        this.playerId = playerId;
        this.location = location;
    }
}
