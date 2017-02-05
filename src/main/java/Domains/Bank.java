package Domains;

import objects.Account;
import objects.Command;
import objects.Parameters;

import java.util.*;

public class Bank extends BaseDomain{

    public HashMap<String, Account> accounts = new HashMap<>();

    public Bank(String name, String domain, String ip, DomainType type) {
        super(name, domain, ip, type);
    }

    public Bank(String name, String domain, String ip, HashMap<String, Command> commands, DomainType type) {
        super(name, domain, ip, commands, type);
    }

    public String transfer(String from, String to, float sum) {
        // validity checks
        Account fromAccount = accounts.get(from);
        if (fromAccount == null)
            return Parameters.BankErrorCannotFindAccountPrefix + from;

        Account toAccount = accounts.get(to);
        if (toAccount == null)
            return Parameters.BankErrorCannotFindAccountPrefix + to;

        return transfer(fromAccount, toAccount, sum);
    }

    public String transfer(Account from, Account to, float sum) {
        // validity checks
        if (from == null)
            return Parameters.BankErrorWithdrawAccountNull;
        if (to == null)
            return Parameters.BankErrorTransferAccountNull;
        if (!from.canTransfer(sum))
            return "Error : account " + from.accountId + " doesn't have enough cash";

        // transfer
        from.changeBalance(-sum);
        to.changeBalance(sum);

        return null;
    }
}
