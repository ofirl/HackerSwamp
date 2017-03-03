package domains;

import database_objects.CommandsTableRow;
import interface_objects.DatabaseHandler;
import interface_objects.DatabaseTables;
import interface_objects.LoginHandler;
import loots.BaseLoot;
import loots.ItemLoot;
import loots.LootType;
import loots.MoneyLoot;
import managers.CommandManager;
import objects.*;
import obstacles.Obstacle;
import obstacles.ObstacleState;

import java.lang.reflect.Method;
import java.util.*;

public abstract class BaseDomain {
    public int id;
    public String name;
    public String domain;
    public String ip;
    public HashMap<String, Command> commands = new HashMap<>();
    public DomainType type;
    public List<Obstacle> obstacles = new ArrayList<>();
    public List<BaseLoot> loot = new ArrayList<>();

    public BaseDomain(int id, String name, String domain, String ip, DomainType type, int lootTier) {
        this(id, name, domain, ip, null, type, randomizeLoot(lootTier));
    }

    public BaseDomain(int id, String name, String domain, String ip, DomainType type, List<BaseLoot> loot) {
        this(id, name, domain, ip, null, type, loot);
    }

    public BaseDomain(int id, String name, String domain, String ip, HashMap<String, Command> commands, DomainType type, int lootTier) {
        this(id, name, domain, ip, commands, type, randomizeLoot(lootTier));
    }

    public BaseDomain(int id, String name, String domain, String ip, HashMap<String, Command> commands, DomainType type, List<BaseLoot> loot) {
        this.id = id;
        this.name = name;
        this.domain = domain;
        this.ip = ip;
        this.commands = commands == null ? new HashMap<>() : commands;
        this.type = type;
        this.loot = loot == null ? new ArrayList<>() : loot;
        if (loot == null) {

        }

        if (commands == null) {
            List<CommandsTableRow> locationCommands = DatabaseHandler.getTableElements(DatabaseTables.Location_Commands, null, "location=" + id);
            if (locationCommands == null)
                return;

            for (CommandsTableRow c :
                    locationCommands)
                addCommand(CommandManager.getCommandById(c.id));
        }
    }

    public static List<BaseLoot> randomizeLoot(int lootTier) {
        List<BaseLoot> lootList = new ArrayList<>();
        Random rand = new Random();
        switch (lootTier) {
            case 0 :
                return lootList;
            case 1 :
                // guaranteed money loot
                int amount = (int)(Parameters.LootMoneyT1MinAmount + (Parameters.LootMoneyT1MaxAmount - Parameters.LootMoneyT1MinAmount) * rand.nextDouble());
                lootList.add(new MoneyLoot(LootType.Money, amount));
                // chance for item loot
                if (rand.nextDouble() < Parameters.LootItemT1Chance) {
                    lootList.add(new ItemLoot(LootType.Item, randomizeItemLoot(lootTier)));
                }
        }

        return lootList;
    }

    public static int randomizeItemLoot(int lootTier) {
        // TODO : implement!
        return 2;
    }

    public void addCommand(Command command) {
        commands.put(command.name, command);
    }

    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    public String connect(CommandContext context, HashMap<String, Argument> args) {
        // check obstacles state
        for (Obstacle o :
                obstacles) {
            if (o.getStateForUser(context.username) != ObstacleState.Disabled)
                return " Error : cannot connect, " + o.name + " must be removed first";
        }

        // change active user location
        ActiveUser a = LoginHandler.getActiveUserByUsername(context.username);
        if ( a == null)
            return Parameters.ErrorActiveUserNotFound;

        a.context.changeLocation(domain);

        return Parameters.DomainConnectedSuccessfully;
    }

    /**
     * executes the provided {@code command}
     * @param command the command to execute
     * @return error or null if everything is ok
     */
    public String executeCommand(CommandContext context, String command, List<Argument> args) {
        try {
            Method method = getClass().getDeclaredMethod(command);
            return (String)method.invoke(this);
        }
        catch (Exception e) {
            return "Error : could not find command " + command + " in " + domain;
        }
    }

    public List<BaseLoot> getLoot() {
        return loot;
    }

    public void ClearLoot() {
        loot.clear();
    }

    public String ClearLoot(String username) {
        String output = "";
        for (BaseLoot l :
                loot)
            output += l.acquire(username);

        loot.clear();
        return output;
    }
}
