package managers;

import objects.PlayerContext;

import java.util.HashMap;

public class ContextManager {
    // static variables
    public static HashMap<String, PlayerContext> contexts = new HashMap<>();

    public static boolean addContext(PlayerContext context) {
        contexts.put(context.username, context);
        return true;
    }
}
