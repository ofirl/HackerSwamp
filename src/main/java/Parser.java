import objects.*;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedTransferQueue;

/**
 * general class for communication between the {@code WebListener} and {@code worker} dyno
 * handles the {@link #parseQueue} and {@link #responseHashMap}
 */
public class Parser {
    // parsing queue
    /**
     * concurrent queue for transferring received commands from {@code WebListener} to workers
     */
    private static LinkedTransferQueue<CommandRequest> parseQueue = new LinkedTransferQueue<>();

    // response queue
    /**
     * concurrent hash map for transferring responses back to the {@code WebListener}
     */
    private static ConcurrentHashMap<String, CommandRequest> responseHashMap = new ConcurrentHashMap<>();

    /**
     * transfers {@code c} to a worker
     * @param c the element to enqueue
     */
    public static void transferCommand(CommandRequest c) {
        try {
            parseQueue.transfer(c);
        }
        catch (Exception e) { }
    }

    /**
     * safely enqueues {@code c} to {@link #parseQueue}
     * @param c the element to enqueue
     */
    public static void responseEnqueue(CommandRequest c) {
        responseHashMap.put(c.username + "-" + c.command ,c);
    }

    /**
     * add a response and notify the relevant object
     * @param key the key to add the response to
     * @param response the response
     */
    public static void addResponse(String key, String response) {
        synchronized (responseHashMap.get(key)) {
            CommandRequest responseCommandRequest = responseHashMap.get(key);
            responseCommandRequest.response = response;
            responseCommandRequest.notify();
        }
    }

    /**
     * safely dequeues an element from {@link #parseQueue}
     * @return the top element in {@link #parseQueue} if one exists, null if the queue is empty
     */
    public static CommandRequest responseDequeue(String key) {
        return responseHashMap.get(key);
    }

    /**
     * returns when response is received for {@code key}
     * @param key the key to wait for response for
     * @return a response matching the key
     */
    public static String waitForResponse(String key) {
        synchronized (responseHashMap.get(key)) {
            CommandRequest responseCommandRequest = responseHashMap.get(key);
            try {
                while (responseCommandRequest.response == null)
                    responseCommandRequest.wait();
            }
            catch (Exception e) { }

            responseHashMap.remove(key);
            return responseCommandRequest.response;
        }
    }

    /**
     * transfers a command to a worker
     * @param c the command to run
     * @return response for the command
     */
    public static void addCommand(CommandRequest c) {
        responseEnqueue(c);
        transferCommand(c);
    }

    public static String requestResponse(String input) {
        HashMap<String,String> args = decodeArgumentsList(input);
        // validity check
        if (!args.containsKey("authKey"))
            return Parameters.parserErrorNoAuthKey;
        if (LoginHandler.getUsernameByKey(args.get("authKey")) == null)
            return Parameters.parserErrorBadAuthKey;
        if (!args.containsKey("command") || !args.containsKey("username"))
            return Parameters.parserErrorInvalidArguments;

        CommandRequest c = new CommandRequest(args.get("username"), args.get("command"));

        addCommand(c);
        return waitForResponse(c.username + "-" + c.command);
    }

    public static String encodeArgument(String name, String arg) {
        return name + "(" + arg.length() + ")=" + arg;
    }

    public static String encodeArgumentList(String[] name, String[] arg) {
        //TODO : write
        return "";
    }

    public static HashMap<String, String> decodeArgumentsList(String args) {
        HashMap<String, String> output = new HashMap<>();

        int nameStart = 0;

        while (nameStart < args.length()) {
            int lengthStart = args.indexOf('(', nameStart);
            String name = args.substring(nameStart, lengthStart);
            int lengthEnd = args.indexOf(')', lengthStart);
            int lengthValue = Integer.parseInt(args.substring(lengthStart + 1, lengthEnd));
            String value = args.substring(lengthStart + 2, lengthStart + 2 + lengthValue);
            nameStart = lengthStart + 2 + lengthValue + 1;

            output.put(name, value);
        }

        return  output;
    }
}
