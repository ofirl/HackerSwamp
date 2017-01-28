package objects;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ThreadedJob extends Thread {
    private Thread thread;
    private long threadId;
    private String name;
    private Method entryPoint;
    private CommandRequest runRequest;

    ThreadedJob (Method entryPoint, CommandRequest runRequest, String name) {
        this.entryPoint = entryPoint;
        this.runRequest = runRequest;
        this.name = name;

        thread = new Thread(this, name);
        threadId = thread.getId();
    }

    public void start () {
        thread.start();
    }

    public void run () {
        // try catch to avoid errors
        try {
            entryPoint.invoke(this);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // TODO : add error report
            runRequest.response = "Error running the runRequest " + runRequest.command;
        }
    }
}
