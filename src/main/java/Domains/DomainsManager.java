package Domains;

import Commands.BaseCommand;
import objects.Account;
import objects.Command;
import objects.CommandAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DomainsManager {

    // key == name
    public static ConcurrentHashMap<String, BaseDomain> allDomains = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Bank> bankDomains = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Company> companyDomains = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Organization> organizationDomains = new ConcurrentHashMap<>();

    // domains initializer
    static {
        // banks
        addBank("First Bank", "first.bank.cash", null);

        // companies

        // organizations
    }

    /**
     * adds n bank
     * @param name the name of the bank
     * @param domain the domain of the bank
     * @param ip the ip of the bank
     */
    public static void addBank(String name, String domain, String ip) {
        Bank b = new Bank(name, domain, ip, DomainType.Bank);
        allDomains.put(name, b);
        bankDomains.put(name, b);
    }

    /**
     * adds a company
     * @param name the name of the company
     * @param domain the domain of the company
     * @param ip the ip of the company
     */
    public static void addCompany(String name, String domain, String ip) {
        Company c = new Company(name, domain, ip, DomainType.Company);
        allDomains.put(name, c);
        companyDomains.put(name, c);
    }

    /**
     * adds an organization
     * @param name the name of the organization
     * @param domain the domain of the organization
     * @param ip the ip of the organization
     */
    public static void addOrganization(String name, String domain, String ip) {
        Organization o = new Organization(name, domain, ip, DomainType.Organization);
        allDomains.put(name, o);
        organizationDomains.put(name, o);
    }

    /**
     * adds a domain
     * @param name the name of the domain
     * @param domain the domain of the domain
     * @param ip the ip of the domain
     * @param type the type of the domain
     */
    public static void addDomain(String name, String domain, String ip, DomainType type) {
        if (type == DomainType.Bank)
            addBank(name, domain, ip);
        else if (type == DomainType.Company)
            addCompany(name, domain, ip);
        else if (type == DomainType.Organization)
            addOrganization(name, domain, ip);
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
    public static List<Account> getBankAccountByUsername(String username) {
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
     * gts a domain by it's name
     * @param name the name to search for
     * @return the domain, or null if one isn't found
     */
    public static BaseDomain getDomainByName(String name) {
        return allDomains.get(name);
    }
}
