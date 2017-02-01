package interface_objects;

import objects.DatabaseQuery;

import java.sql.ResultSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedTransferQueue;

public class DatabaseHandler {
    /**
     * queries request queue
     */
    public static LinkedTransferQueue<DatabaseQuery> queryQueue = new LinkedTransferQueue<>();

    /**
     * queries response queue
     */
    public static ConcurrentHashMap<Integer, DatabaseQuery> resultQueue = new ConcurrentHashMap<>();

    public static int currentId = 0;

    /**
     * transfers {@code c} to a worker
     * @param query the element to enqueue
     */
    public static void transferQuery(DatabaseQuery query) {
        try {
            queryQueue.transfer(query);
        }
        catch (Exception e) { }
    }

    /**
     * receives a query from the queue, blocking
     * @return the top query from the queue
     */
    public static DatabaseQuery receiveQuery() {
        DatabaseQuery output = null;
        try {
            output = queryQueue.take();
        }
        catch (Exception e) { }

        return output;
    }

    /**
     * safely enqueues {@code c} to {@link #queryQueue}
     * @param query the element to enqueue
     */
    public static void responseEnqueue(DatabaseQuery query) {
        resultQueue.put(query.id ,query);
    }

    /**
     * add a response and notify the relevant object
     * @param key the key to add the response to
     * @param response the response
     */
    public static void addResponse(int key, ResultSet response) {
        synchronized (resultQueue.get(key)) {
            DatabaseQuery responseCommandRequest = resultQueue.get(key);
            responseCommandRequest.result = response;
            responseCommandRequest.notify();
        }
    }

    /**
     * safely dequeues an element from {@link #queryQueue}
     * @return the top element in {@link #queryQueue} if one exists, null if the queue is empty
     */
    public static DatabaseQuery responseDequeue(int key) {
        return resultQueue.get(key);
    }

    /**
     * returns when response is received for {@code key}
     * @param key the key to wait for response for
     * @return a response matching the key
     */
    public static ResultSet waitForResponse(int key) {
        synchronized (resultQueue.get(key)) {
            DatabaseQuery responseCommandRequest = resultQueue.get(key);
            try {
                while (responseCommandRequest.result == null)
                    responseCommandRequest.wait();
            }
            catch (Exception e) { }

            resultQueue.remove(key);
            return responseCommandRequest.result;
        }
    }

    /**
     * transfers a command to a worker
     * @param query the command to run
     * @return response for the command
     */
    public static void addQuery(DatabaseQuery query) {
        responseEnqueue(query);
        transferQuery(query);
    }

    public static ResultSet requestResponse(String input) {
        // validity check
        if (input == null || input.equals(""))
            return null;

        int queryKey = getNextQueryId();
        DatabaseQuery c = new DatabaseQuery(input, queryKey);

        addQuery(c);
        return waitForResponse(queryKey);
    }

    public static int getNextQueryId() {
        currentId++;
        return currentId - 1;
    }
}
