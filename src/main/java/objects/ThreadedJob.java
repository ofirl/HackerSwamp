package objects;

import interfaces.*;

public class ThreadedJob extends Thread {
    public Thread thread;
    public long threadId;
    public String name;
    private ThreadEntryPoint threadEntryPoint;
    private ThreadOnFinish threadOnFinished;
    private Object[] args;

    /**
     *
     * @param entryPoint the entry point entry of the thread
     * @param args
     * @param name
     * @param onFinish
     */
    ThreadedJob (ThreadEntryPoint entryPoint, Object[] args, String name, ThreadOnFinish onFinish) {
        threadEntryPoint = entryPoint;
        this.args = args;
        this.name = name;
        threadOnFinished = onFinish;

        thread = new Thread(this, name);
        threadId = thread.getId();
    }

    public void start () {
        thread.start();
    }

    public void run () {
        String error = null;
        CommandRequest errorOutput = null;

        // try catch to avoid errors
        try {
            if (args != null) {
                errorOutput = args[0].getClass() == CommandRequest.class ? (CommandRequest) args[0] : null;
                threadEntryPoint.entryPoint();
            }
            else
                threadEntryPoint.entryPoint();
        } catch (Exception e) {
            error = e.getMessage();
            if (errorOutput != null)
                errorOutput.response = "Error running the requestToHandle : " + errorOutput.command;
        }

        threadOnFinished.onFinish(this);

        if (error != null)
            System.err.println("Error : exception caught on thread : " + thread.getName() + " - " + error);
    }
}
