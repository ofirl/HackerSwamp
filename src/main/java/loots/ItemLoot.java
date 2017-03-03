package loots;

import interface_objects.LoginHandler;
import items.BaseItem;
import managers.ItemManager;
import objects.Parameters;

public class ItemLoot extends BaseLoot {
    // public variables
    public int itemId;

    public ItemLoot(LootType type, int itemId) {
        super(type);
        this.itemId = itemId;
    }

    @Override
    public String acquire(String username) {
        BaseItem item = ItemManager.getItemById(itemId);
        if (item == null)
            return Parameters.ErrorMarketItemNotFound;

        if (!ItemManager.addItemToUserInventory(username, itemId))
            return "Error : could not insert " + item.name  + " to your inventory";

        return item.name + " has been added to your inventory";
    }
}
