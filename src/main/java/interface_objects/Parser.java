package interface_objects;

import objects.*;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedTransferQueue;

/**
 * general class for communication between the {@code processes.WebListener} and {@code worker} dyno
 * handles the {@link #parseQueue} and {@link #responseHashMap}
 */
public class Parser {
    // parsing queue
    /**
     * concurrent queue for transferring received commands from {@code processes.WebListener} to workers
     */
    private static LinkedTransferQueue<CommandRequest> parseQueue = new LinkedTransferQueue<>();

    // response queue
    /**
     * concurrent hash map for transferring responses back to the {@code processes.WebListener}
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
     * receives a command from the queue, blocking
     * @return the top command from the queue
     */
    public static CommandRequest receiveCommand() {
        CommandRequest output = null;
        try {
            output = parseQueue.take();
        }
        catch (Exception e) { }

        return output;
    }

    /**
     * safely enqueues {@code c} to {@link #parseQueue}
     * @param c the element to enqueue
     */
    public static void responseEnqueue(CommandRequest c) {
        responseHashMap.put(c.getKey() ,c);
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

    /**
     * requests and waits for response to {@code input}
     * @param input the request received
     * @return a response
     */
    public static String requestResponse(String input) {
        HashMap<String,String> args = decodeArgumentsList(input);
        // validity check
        if (!args.containsKey("authKey"))
            return Parameters.parserErrorNoAuthKey;
        if (LoginHandler.getUsernameByKey(args.get("authKey")) == null)
            return Parameters.parserErrorBadAuthKey;
        if (!args.containsKey("command") || !args.containsKey("username"))
            return Parameters.parserErrorInvalidArguments;

        ActiveUser activeUser = LoginHandler.getActiveUserByKey(args.get("authKey"));
        CommandContext context = new CommandContext(activeUser.username, activeUser.playerId, activeUser.getLocation());
        CommandRequest c = new CommandRequest(args.get("command"), context);

        addCommand(c);
        return waitForResponse(c.getKey());
    }

    /**
     * same as {@link #encodeArgumentList(HashMap)} but for single argument
     * @param name the name of the argument
     * @param value the value of the argument
     * @return encoded argument as string
     */
    public static String encodeArgument(String name, String value) {
        HashMap<String, String> arg = new HashMap<>();
        arg.put(name, value);
        return encodeArgumentList(arg);
    }

    /**
     * encodes a {@link HashMap} of key value pairs as string for adding to a response
     * @param args {@link HashMap} of the key value pairs to encode
     * @return encoded arguments as a string
     */
    public static String encodeArgumentList(HashMap<String, String> args) {
        String output = "";
        for (String key :
                args.keySet()) {
            String value = args.get(key);
            output += key + "(" + value.length() + ")=" + value + "&"; // key(length)=value&
        }

        if (!output.equals(""))
            output = output.substring(0, output.length() - 1);

        return output;
    }

    /**
     * decodes received parameters (in the requestToHandle body)
     * received parameters are of the form : key1(length)=value1&key2(length)=value2
     * @param args arguments string to decode-
     * @return {@link HashMap} of the keys and values extracted
     */
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
