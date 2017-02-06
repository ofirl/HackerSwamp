package Domains;

import objects.Command;

import java.util.*;

public abstract class BaseDomain {
    public String name;
    public String domain;
    public String ip;
    public HashMap<String, Command> commands;
    public DomainType type;

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

    public abstract String executeCommand(String command);
}
