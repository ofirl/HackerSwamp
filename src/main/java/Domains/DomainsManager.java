package Domains;

import Commands.BaseCommand;
import objects.Command;
import objects.CommandAccess;

import java.util.concurrent.ConcurrentHashMap;

public class DomainsManager {

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

    public static void addBank(String name, String domain, String ip) {
        Bank b = new Bank(name, domain, ip, DomainType.Bank);
        allDomains.put(name, b);
        bankDomains.put(name, b);
    }

    public static void addCompany(String name, String domain, String ip) {
        Company c = new Company(name, domain, ip, DomainType.Company);
        allDomains.put(name, c);
        companyDomains.put(name, c);
    }

    public static void addOrganization(String name, String domain, String ip) {
        Organization o = new Organization(name, domain, ip, DomainType.Organization);
        allDomains.put(name, o);
        organizationDomains.put(name, o);
    }

    public static void addDomain(String name, String domain, String ip, DomainType type) {
        if (type == DomainType.Bank)
            addBank(name, domain, ip);
        else if (type == DomainType.Company)
            addCompany(name, domain, ip);
        else if (type == DomainType.Organization)
            addOrganization(name, domain, ip);
    }
}
