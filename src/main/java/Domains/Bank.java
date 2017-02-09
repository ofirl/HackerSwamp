package Domains;

import objects.*;

import java.util.*;

public class Bank extends BaseDomain{

    /**
     * list of all account ids already assigned
     */
    public static List<String> accountIdList = new ArrayList<>();
    /**
     * accounts {@link HashMap}, key is account id
     */
    public HashMap<String, Account> accounts = new HashMap<>();

    /**
     * constructor
     * @param name
     * @param domain
     * @param ip
     * @param type
     */
    public Bank(String name, String domain, String ip, DomainType type) {
        this(name, domain, ip, null, type);
    }

    /**
     * constructor
     * @param name
     * @param domain
     * @param ip
     * @param commands
     * @param type
     */
    public Bank(String name, String domain, String ip, HashMap<String, Command> commands, DomainType type) {
        super(name, domain, ip, commands, type);

        // TODO : pull accounts data from db and add them
    }

    /**
     * gets a random account id, that does not already exists
     * @return a new account id
     */
    public static String getRandomAccountId() {
        String accountId = "";
        Random rand = new Random();

        do {
            for (int i = 0; i < 16; i++) {
                if (rand.nextDouble() > 0.5)
                    accountId += rand.nextInt(10); // 0-9
                else
                    accountId += (char)rand.nextInt(26) + 65; // 65-90
            }
        }
        while (accountIdList.contains(accountId));
        accountIdList.add(accountId);

        return accountId;
    }

    /**
     * adds an account to the bank
     * @param owner the account owner
     * @return whether the insertion succeeded
     */
    public boolean addAccount(String owner) {
        if (getAccountByUsername(owner) != null)
            return false;

        String id = getRandomAccountId();
        accounts.put(id ,new Account(owner, id, this));
        return true;
    }

    /**
     * transfer {@code sum} from {@code from} to {@code to}
     * @param from the account id to transfer from
     * @param to the account id to transfer to
     * @param sum the sum to transfer
     * @return null if succeeded, error otherwise
     */
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

    /**
     * transfer {@code sum} from {@code from} to {@code to}
     * @param from the account to transfer from
     * @param to the account to transfer to
     * @param sum the sum to transfer
     * @return null if succeeded, error otherwise
     */
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

    /**
     * gets the {@link Account} object that matches the {@code id} provided
     * @param id the id to search for
     * @return an {@link Account} object or null if not found
     */
    public Account getAccountById(String id) {
        return accounts.get(id);
    }

    /**
     * gets the {@link Account} object that matches the {@code username} provided
     * @param username the username to search for
     * @return an {@link Account} object or null if not found
     */
    public Account getAccountByUsername(String username) {
        for (Account a :
                accounts.values()) {
            if (a.owner.equals(username))
                return a;
        }

        return null;
    }
}
