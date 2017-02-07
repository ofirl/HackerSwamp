package Domains;

import objects.*;

import java.util.*;

public class Company extends BaseDomain{

    public Company(String name, String domain, String ip, DomainType type) {
        super(name, domain, ip, type);
    }

    public Company(String name, String domain, String ip, HashMap<String, Command> commands, DomainType type) {
        super(name, domain, ip, commands, type);
    }

    /**
     * executes the provided {@code command}
     * @param command the command to execute
     * @return error or null if everything is ok
     */
    public String executeCommand(CommandContext context, String command, List<Argument> args) {
        // TODO : write the code for the executeCommand function
        return null;
    }
}
