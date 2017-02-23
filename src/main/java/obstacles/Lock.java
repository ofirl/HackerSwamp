package obstacles;

// TODO : implement T1-? locks
public abstract class Lock extends Obstacle{

    public Lock(int id, String name) {
        super(id ,name, ObstacleType.Lock);
    }
}
