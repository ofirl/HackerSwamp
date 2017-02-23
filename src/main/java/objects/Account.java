package objects;

import domains.Bank;

/**
 * account object
 * <br/>
 * <br/>
 * attributes :
 * <ul>
 *     <li> {@link #owner} </li>
 *     <li> {@link #accountId} </li>
 *     <li> {@link #balance} </li>
 *     <li> {@link #bank} </li>
 * </ul>
 */
public class Account {
    public final String owner;
    public final String accountId;
    public double balance;
    public final Bank bank;

    /**
     * constructor
     * @param owner owner of the account (username)
     * @param accountId id of the account
     * @param bank bank of the account
     */
    public Account(String owner, String accountId, Bank bank) {
        this(owner, accountId, 0, bank);
    }

    /**
     * constructor
     * @param owner owner of the account (username)
     * @param accountId id of the account
     * @param balance balance of the account
     * @param bank bank of the account
     */
    public Account(String owner, String accountId, double balance, Bank bank) {
        this.owner = owner;
        this.accountId = accountId;
        this.balance = balance;
        this.bank = bank;
    }

    /**
     * checks if the account can transfer {@code sum}
     * @param sum the amount to check for
     * @return whether the account can transfer {@code sum}
     */
    public boolean canTransfer(float sum) {
        return balance >= sum;
    }

    /**
     * changes the balance of the account by {@code sum} (can be positive or negative) <br/>
     * NO checks are being made!!! account can get negative!!!
     * @param sum the amount to change by
     */
    public void changeBalance(float sum) {
        balance += sum;
    }
}
