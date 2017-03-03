package loots;

import interface_objects.LoginHandler;
import objects.Account;
import objects.ActiveUser;
import objects.Parameters;

// TODO : change name of "Money" to <TBD>
public class MoneyLoot extends BaseLoot {
    // public variables
    public int amount;

    public MoneyLoot(LootType type, int amount) {
        super(type);
        this.amount = amount;
    }

    @Override
    public String acquire(String username) {
        ActiveUser activeUser = LoginHandler.getActiveUserByUsername(username);
        if (activeUser == null)
            return Parameters.ErrorActiveUserNotFound;

        Account acc = activeUser.getMainAccount();
        if (acc == null)
            return Parameters.ErrorMainAccountNotFound;

        acc.changeBalance(amount);
        return amount + " money was transferred to your account";
    }
}
