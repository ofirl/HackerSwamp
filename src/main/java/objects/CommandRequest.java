package objects;

/**
 * CommandRequest object
 * {} the command that was received by the {@code webListener} process
 */
public class CommandRequest {
    public String command;
    public String response;
    public CommandContext context;
    public String key;

    public CommandRequest (String command, CommandContext context) {
        this.command = command;
        this.context = context;
        this.key = context.username + "-" + command;
    }

    public String getKey() {
        return key;
    }
}
