import objects.*;
import org.eclipse.jetty.util.ArrayQueue;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Parser {
    // parsing queue
    public static ArrayQueue<CommandRequest> parseQueue = new ArrayQueue<>();
    public static ReentrantLock parseQueueLock = new ReentrantLock();

    // response queue
    public static HashMap<Integer, CommandRequest> responseQueue = new HashMap<>();
    public static ReentrantLock responseQueueLock = new ReentrantLock();

    public static void Enqueue(CommandRequest c) {
        parseQueueLock.lock();
        try {
            parseQueue.add(c);
        }
        finally {
            parseQueueLock.unlock();
        }
    }
}
