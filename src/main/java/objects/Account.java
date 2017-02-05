package objects;

public class Account {
    public String owner;
    public String accountId;
    public float balance;

    public Account(String owner, String accountId) {
        this(owner, accountId, 0);
    }

    public Account(String owner, String accountId, float balance) {
        this.owner = owner;
        this.accountId = accountId;
        this.balance = balance;
    }

    public boolean canTransfer(float sum) {
        return balance >= sum;
    }

    public void changeBalance(float sum) {
        balance += sum;
    }
}
