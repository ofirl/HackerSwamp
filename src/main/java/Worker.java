import objects.*;
import interface_objects.*;

public class Worker {

    public static CommandRequest requestToHandle;
    public static ThreadedJobFactory threadFactory = new ThreadedJobFactory(Parameters.maxWorkerThreads);
    public CommandRequest request;

    public static void main(String[] args) {
        while (true) {
            requestToHandle = null;
            // make sure we have a requestToHandle
            while (requestToHandle == null)
                requestToHandle = Parser.receiveCommand();

            // create a thread and handle the requestToHandle
            threadFactory.newThread(new Worker(requestToHandle)::workerStart, null, "workerThread");
        }
    }

    /**
     * constructor
     * @param r the request to handle
     */
    public Worker(CommandRequest r) {
        request = r;
    }

    public void workerStart(Object... args) {
        // TODO : work on worker logic

        Parser.addResponse(request.getKey(), request.getKey());
    }
}
