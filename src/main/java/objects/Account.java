package objects;

import Domains.Bank;

public class Account {
    public String owner;
    public String accountId;
    public float balance;
    public Bank bank;

    public Account(String owner, String accountId, Bank bank) {
        this(owner, accountId, 0, bank);
    }

    public Account(String owner, String accountId, float balance, Bank bank) {
        this.owner = owner;
        this.accountId = accountId;
        this.balance = balance;
        this.bank = bank;
    }

    public boolean canTransfer(float sum) {
        return balance >= sum;
    }

    public void changeBalance(float sum) {
        balance += sum;
    }
}
