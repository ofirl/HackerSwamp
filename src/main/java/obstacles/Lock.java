package obstacles;

// TODO : implement T1-? locks
public abstract class Lock extends Obstacle{

    public Lock(String name) {
        super(name, ObstacleType.Lock);
    }
}
