package objects;

/**
 * CommandRequest object
 * {} the command that was received by the {@code webListener} process
 */
public class CommandRequest {
    public String username;
    public String command;
    public String response;

    public CommandRequest (String username, String command) {
        this.username = username;
        this.command = command;
    }
}
