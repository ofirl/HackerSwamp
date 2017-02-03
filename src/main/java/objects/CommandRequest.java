package objects;

/**
 * CommandRequest object
 * {} the command that was received by the {@code webListener} process
 */
public class CommandRequest {
    public String username;
    public String command;
    public String response;
    public CommandContext context;

    public CommandRequest (String username, String command, CommandContext context) {
        this.username = username;
        this.command = command;
        this.context = context;
    }

    public String getKey() {
        return username + "-" + command;
    }
}
