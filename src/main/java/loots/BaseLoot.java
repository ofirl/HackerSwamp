package loots;

public abstract class BaseLoot {
    public LootType type;

    public BaseLoot(LootType type) {
        this.type = type;
    }

    public abstract String acquire(String username);
}
