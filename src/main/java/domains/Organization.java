package domains;

import objects.*;

import java.util.*;

public class Organization extends BaseDomain{

    public Organization(int id, String name, String domain, String ip, DomainType type) {
        super(id, name, domain, ip, type);
    }

    public Organization(int id, String name, String domain, String ip, HashMap<String, Command> commands, DomainType type) {
        super(id, name, domain, ip, commands, type);
    }
}
