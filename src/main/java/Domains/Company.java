package Domains;

import objects.Command;

import java.util.HashMap;

public class Company extends BaseDomain{

    public Company(String name, String domain, String ip, DomainType type) {
        super(name, domain, ip, type);
    }

    public Company(String name, String domain, String ip, HashMap<String, Command> commands, DomainType type) {
        super(name, domain, ip, commands, type);
    }
}
