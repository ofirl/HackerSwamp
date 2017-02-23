package domains;

import interface_objects.LoginHandler;
import objects.*;
import obstacles.Obstacle;
import obstacles.ObstacleState;

import java.lang.reflect.Method;
import java.util.*;

public abstract class BaseDomain {
    public int id;
    public String name;
    public String domain;
    public String ip;
    public HashMap<String, Command> commands;
    public DomainType type;
    public List<Obstacle> obstacles = new ArrayList<>();

    public BaseDomain(int id, String name, String domain, String ip, DomainType type) {
        this.id = id;
        this.name = name;
        this.domain = domain;
        this.ip = ip;
        this.commands = new HashMap<>();
        this.type = type;
    }

    public BaseDomain(int id, String name, String domain, String ip, HashMap<String, Command> commands, DomainType type) {
        this.id = id;
        this.name = name;
        this.domain = domain;
        this.ip = ip;
        this.commands = commands;
        this.type = type;
    }

    public void addCommand(Command command) {
        commands.put(command.name, command);
    }

    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    public String connect(CommandContext context, HashMap<String, Argument> args) {
        // check obstacles state
        for (Obstacle o :
                obstacles) {
            if (o.getStateForUser(context.username) != ObstacleState.Disabled)
                return " Error : cannot connect, " + o.name + " must be removed first";
        }

        // change active user location
        ActiveUser a = LoginHandler.getActiveUserByUsername(context.username);
        if ( a == null)
            return Parameters.ErrorActiveUserNotFound;

        a.context.changeLocation(domain);

        return Parameters.DomainConnectedSuccessfully;
    }

    /**
     * executes the provided {@code command}
     * @param command the command to execute
     * @return error or null if everything is ok
     */
    public String executeCommand(CommandContext context, String command, List<Argument> args) {
        try {
            Method method = getClass().getDeclaredMethod(command);
            return (String)method.invoke(this);
        }
        catch (Exception e) {
            return "Error : could not find command " + command + " in " + domain;
        }
    }
}
