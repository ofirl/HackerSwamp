package domains;

import database_objects.CommandsTableRow;
import interface_objects.DatabaseHandler;
import interface_objects.DatabaseTables;
import interface_objects.LoginHandler;
import items.Software;
import loots.BaseLoot;
import loots.ItemLoot;
import loots.LootType;
import loots.MoneyLoot;
import managers.CommandManager;
import managers.ItemManager;
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
    public SystemSpec spec;
    public HashMap<Integer, Software> installedSoftware;
    public HashMap<Integer, Software> softwareInventory;

    /**
     * constructor
     */
    public BaseDomain(int id, String name, String domain, String ip, DomainType type, int lootTier) {
        this(id, name, domain, ip, null, type, randomizeLoot(lootTier));
    }

    /**
     * constructor
     */
    public BaseDomain(int id, String name, String domain, String ip, DomainType type, List<BaseLoot> loot) {
        this(id, name, domain, ip, null, type, loot);
    }

    /**
     * constructor
     */
    public BaseDomain(int id, String name, String domain, String ip, HashMap<String, Command> commands, DomainType type, int lootTier) {
        this(id, name, domain, ip, commands, type, randomizeLoot(lootTier));
    }

    /**
     * constructor
     */
    public BaseDomain(int id, String name, String domain, String ip, HashMap<String, Command> commands, DomainType type, List<BaseLoot> loot) {
        this.id = id;
        this.name = name;
        this.domain = domain;
        this.ip = ip;
        this.commands = commands == null ? new HashMap<>() : commands;
        this.type = type;
        this.loot = loot == null ? new ArrayList<>() : loot;
        this.spec = SystemSpec.getUserSystemSpecs(name);
        this.installedSoftware = ItemManager.getUserInstalledSoftware(name);
        this.softwareInventory = ItemManager.getAllLocationSoftware(name);

        if (commands == null) {
            List<CommandsTableRow> locationCommands = DatabaseHandler.getTableElements(DatabaseTables.Location_Commands, null, "location=" + id);
            if (locationCommands == null)
                return;

            for (CommandsTableRow c :
                    locationCommands)
                addCommand(CommandManager.getCommandById(c.id));
        }
    }

    /**
     * return a randomized loot based on the {@code lootTier}
     * @param lootTier loot tier
     * @return randomized loot
     */
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

    /**
     * return a random item based on {@code lootTier}
     * @param lootTier tier of the item
     * @return random item
     */
    public static int randomizeItemLoot(int lootTier) {
        // TODO : implement!
        return 2;
    }

    /**
     * addss a command to the domain
     * @param command the command to add
     */
    public void addCommand(Command command) {
        commands.put(command.name, command);
    }

    /**
     * adds an obstacle to the domain
     * @param obstacle the obstacle to add
     */
    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    /**
     * gets the software inventory
     * @return software inventory
     */
    public HashMap<Integer, Software> getSoftwareInventory() {
        return softwareInventory;
    }

    /**
     * gets the installed software
     * @return all the installed software
     */
    public HashMap<Integer, Software> getInstalledSoftware() {
        return installedSoftware;
    }

    /**
     * tries to connect to the domain
     * @param context the context of the connection
     * @param args command args
     * @return a response
     */
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

    /**
     * gets the domain loot
     * @return the domains loot
     */
    public List<BaseLoot> getLoot() {
        return loot;
    }

    /**
     * clears the loot
     */
    public void ClearLoot() {
        loot.clear();
    }

    /**
     * clears hte loot and adds it to the provided {@code username}
     */
    public String ClearLoot(String username) {
        String output = "";
        for (BaseLoot l :
                loot)
            output += l.acquire(username);

        loot.clear();
        return output;
    }

    /**
     * gets the available amount of free space
     * @return amount of free space
     */
    public int getAvailableSize() {
        int softareSize = 0;
        for (Software s :
                installedSoftware.values())
            softareSize += s.size;

        return getTotalSize() - softareSize;
    }

    /**
     * gets the total amount of space
     * @return total amount of space
     */
    public int getTotalSize() {
        return spec.getTotalSize();
    }

    /**
     * installs a program
     */
    public void installSoftware(Software software) {
        installedSoftware.put(software.id, software);
        String filter = "location='" + this.name + "' AND costume_name='" + software.costumeName + "'";
        DatabaseHandler.updateTable(DatabaseTables.Inventories_Software, filter, "equipped=true");
    }

    /**
     * uninstalls a program
     */
    public void uninstallSoftware(Software software) {
        installedSoftware.put(software.id, software);
        String filter = "location='" + this.name + "' AND costume_name='" + software.costumeName + "'";
        DatabaseHandler.updateTable(DatabaseTables.Inventories_Software, filter, "equipped=false");
    }

    /**
     * adds a software to the domain
     * @param software software to add
     * @param owner owner of the software
     */
    public void addSoftware(Software software, String owner) {
        softwareInventory.put(software.id, software);
        String columnOrder = "owner, item, location, version, costume_name";
        String columnValues = owner + "," + software.id + "," + this.name + "," + software.version + "," + software.costumeName;
        DatabaseHandler.insertIntoTable(DatabaseTables.Inventories_Software, columnOrder, columnValues);
    }

    /**
     * deletes a software from the domain
     * @param software software to delete
     */
    public void deleteSoftware(Software software) {
        softwareInventory.put(software.id, software);
        String filter = "location='" + this.name + "' AND costume_name='" + software.costumeName + "'";
        DatabaseHandler.removeFromTable(DatabaseTables.Inventories_Software, filter);
    }
}
