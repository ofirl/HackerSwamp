package domains;

import objects.*;

import java.util.*;

public class Organization extends BaseDomain{

    public Organization(String name, String domain, String ip, DomainType type) {
        super(name, domain, ip, type);
    }

    public Organization(String name, String domain, String ip, HashMap<String, Command> commands, DomainType type) {
        super(name, domain, ip, commands, type);
    }
}
