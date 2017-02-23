package domains;

import objects.*;

import java.util.*;

public class Company extends BaseDomain{

    public Company(int id, String name, String domain, String ip, DomainType type) {
        super(id, name, domain, ip, type);
    }

    public Company(int id, String name, String domain, String ip, HashMap<String, Command> commands, DomainType type) {
        super(id, name, domain, ip, commands, type);
    }

}
