package Domains;

import objects.Command;

import java.util.HashMap;

public class Organization extends BaseDomain{

    public Organization(String name, String domain, String ip, DomainType type) {
        super(name, domain, ip, type);
    }

    public Organization(String name, String domain, String ip, HashMap<String, Command> commands, DomainType type) {
        super(name, domain, ip, commands, type);
    }

    /**
     * executes the provided {@code command}
     * @param command the command to execute
     * @return error or null if everything is ok
     */
    public String executeCommand(String command) {
        // TODO : write the code for the executeCommand function
        return null;
    }
}
