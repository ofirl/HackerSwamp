package managers;

import commands.CommandAccess;
import database_objects.DomainsTableRow;
import database_objects.ObstaclesTableRow;
import domains.*;
import interface_objects.DatabaseHandler;
import interface_objects.DatabaseTables;
import objects.*;
import obstacles.Firewall;
import obstacles.Obstacle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DomainsManager {
    private static String className = "DomainsManager";

    // key == name
    public static ConcurrentHashMap<String, BaseDomain> allDomains = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Bank> bankDomains = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Company> companyDomains = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Organization> organizationDomains = new ConcurrentHashMap<>();

    /**
     * domains initializer
     */
    public static void init() {
        // region banks

        List<DomainsTableRow> banks = DatabaseHandler.getTableElements(DatabaseTables.Domains, null, "type='Bank'");
        if (banks == null) {
            Logger.log(className + ".init", Parameters.ErrorDomainsInit);
            return;
        }

        for (DomainsTableRow bankRow :
                banks) {
            // add bank
            Bank b = addBank(bankRow.id, bankRow.name, bankRow.domain, bankRow.ip);
            // add obstacles
            List<ObstaclesTableRow> obstacles = DatabaseHandler.getTableElements(DatabaseTables.Obstacles, null, "domain=" + bankRow.id);
            if (obstacles == null) {
                Logger.log(className + ".init", Parameters.ErrorDomainsInit);
                return;
            }

            for (ObstaclesTableRow o :
                    obstacles) {
                Obstacle obstacleObject = createObstacle(o.id, o.type, o.tier, o.sub_type);
                if (obstacleObject == null) {
                    Logger.log(className + ".init", Parameters.ErrorDomainsInit);
                    return;
                }

                b.addObstacle(obstacleObject);
            }
        }

        // endregion

        // region companies

        // endregion

        // region organizations

        // endregion
    }

    public static Obstacle createObstacle(int id, String type, int tier, String sub_type) {
        switch (type) {
            case "firewall" :
                return new Firewall(id, "firewall " + tier, tier);
            case "lock" :
                // TODO : return a lock object (once implemented)
                return null;
        }

        return null;
    }

    /**
     * adds a bank
     * @param name the name of the bank
     * @param domain the domain of the bank
     * @param ip the ip of the bank
     */
    public static Bank addBank(int id, String name, String domain, String ip) {
        Bank b = new Bank(id, name, domain, ip, DomainType.Bank);
        allDomains.put(domain, b);
        bankDomains.put(domain, b);
        return b;
    }

    /**
     * adds a company
     * @param name the name of the company
     * @param domain the domain of the company
     * @param ip the ip of the company
     */
    public static void addCompany(int id, String name, String domain, String ip) {
        Company c = new Company(id, name, domain, ip, DomainType.Company);
        allDomains.put(domain, c);
        companyDomains.put(domain, c);
    }

    /**
     * adds an organization
     * @param name the name of the organization
     * @param domain the domain of the organization
     * @param ip the ip of the organization
     */
    public static void addOrganization(int id, String name, String domain, String ip) {
        Organization o = new Organization(id, name, domain, ip, DomainType.Organization);
        allDomains.put(domain, o);
        organizationDomains.put(domain, o);
    }

    /**
     * adds a domain
     * @param name the name of the domain
     * @param domain the domain of the domain
     * @param ip the ip of the domain
     * @param type the type of the domain
     */
    public static void addDomain(int id, String name, String domain, String ip, DomainType type) {
        if (type == DomainType.Bank)
            addBank(id, name, domain, ip);
        else if (type == DomainType.Company)
            addCompany(id, name, domain, ip);
        else if (type == DomainType.Organization)
            addOrganization(id, name, domain, ip);
    }

    /**
     * gets the account of the provided {@code id}
     * @param id the id to search for
     * @return account, or null if one isn't found
     */
    public static Account getBankAccountById(String id) {
        for (Bank b :
                bankDomains.values()) {
            Account a = b.getAccountById(id);
            if (a != null)
                return a;
        }

        return null;
    }

    /**
     * gets all the accounts of the provided {@code username}
     * @param username the username to search for
     * @return a list of all the accounts of the username
     */
    public static List<Account> getBankAccountsByUsername(String username) {
        List<Account> accounts = new ArrayList<>();

        for (Bank b :
                bankDomains.values()) {
            Account a = b.getAccountByUsername(username);
            if (a != null)
                accounts.add(a);
        }

        return accounts;
    }

    /**
     * gets the main account for the provided {@code username} (the bit coin account)
     * @param username the username to search for
     * @return the main account of the {@code username}, or null if there isn't one
     */
    public static Account getMainAccountByUsername(String username) {
        List<Account> accounts = getBankAccountsByUsername(username);
        for (Account a :
                accounts) {
            if (a.bank.name.equals(Parameters.MainBankName))
                return a;
        }

        return null;
    }

    /**
     * gts a domain by it's name
     * @param name the name to search for
     * @return the domain, or null if one isn't found
     */
    public static BaseDomain getDomainByName(String name) {
        return allDomains.get(name);
    }

    /**
     * tries to connect to the provided {@code domain}
     * @param domain the domain to connect to
     * @return error message or null if succeeded
     */
    public static String connectToDomain(String domain, CommandContext context, HashMap<String, Argument> args) {
        BaseDomain d = allDomains.get(domain);
        if (d != null)
            return d.connect(context, args);

        return Parameters.ErrorDomainNotFoundPrefix + domain;
    }
}
