package objects;

import Domains.BaseDomain;

import java.util.HashMap;

public class PlayerContext {
    // public variables
    public String username;
    public HashMap<String, BaseDomain> crackedDomains = new HashMap<>();
    public HashMap<String, BaseDomain> lockedDomains = new HashMap<>();

    public PlayerContext(String username) {
        this.username = username;
        // TODO : add cracked domain table to db
        // TODO : pull cracked domains data from db
        //this.crackedDomains = crackedDomains;
        //this.lockedDomains = lockedDomains;
    }
}
