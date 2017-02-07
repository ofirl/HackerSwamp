package Domains;

import interface_objects.LoginHandler;
import objects.*;
import obstacles.Obstacle;
import obstacles.ObstacleState;

import java.util.*;

public abstract class BaseDomain {
    public String name;
    public String domain;
    public String ip;
    public HashMap<String, Command> commands;
    public DomainType type;
    public List<Obstacle> obstacles = new ArrayList<>();

    public BaseDomain(String name, String domain, String ip, DomainType type) {
        this.name = name;
        this.domain = domain;
        this.ip = ip;
        this.commands = new HashMap<>();
        this.type = type;
    }

    public BaseDomain(String name, String domain, String ip, HashMap<String, Command> commands, DomainType type) {
        this.name = name;
        this.domain = domain;
        this.ip = ip;
        this.commands = commands;
        this.type = type;
    }

    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    public String connect(CommandContext context, HashMap<String, Argument> args) {
        // check obstacles state
        for (Obstacle o :
                obstacles) {
            if (o.getStateForUser(context.username) != ObstacleState.Removed)
                return " Error : cannot connect, " + o.name + " must be removed first";
        }

        // change active user location
        // TODO : change context location for the user
        ActiveUser a = LoginHandler.getActiveUserByUsername(context.username);
        if ( a == null)
            return Parameters.ErrorActiveUserNotFound;

        // TODO : domain? or name? need to make sure
        a.location = domain;

        return Parameters.DomainConnectedSuccessfully;
    }

    public abstract String executeCommand(CommandContext context, String command, List<Argument> args);
}
