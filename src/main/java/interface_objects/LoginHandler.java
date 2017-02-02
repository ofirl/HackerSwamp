package interface_objects;

import database_objects.PlayersTableRow;
import objects.Parameters;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static interface_objects.Tables.Players;

/**
 * Handles the login process :
 * <ul>
 * <li> parsing the requestToHandle </li>
 * <li> validating username and password against the database </li>
 * <li> generating authentication key </li>
 * <li> inserts a new entry to the active user list </li>
 * </ul>
 */
public class LoginHandler {
    public static ConcurrentHashMap<String, String> activeUsers = new ConcurrentHashMap<>();

    /**
     * entry point - the method to be called when a new login requestToHandle is received
     * @param input the parameters of the received requestToHandle
     * @return authentication key or error message (starts with "Error")
     */
    public static String checkLogin(String input) {
        // sanity checks
        String[] args = input.split("&");
        if (args.length != 2)
            return Parameters.loginErrorArgumentsCount;

        if (args[0].indexOf('=') == -1 || args[1].indexOf('=') == -1)
            return Parameters.loginErrorArgumentsSyntax;

        // more sanity checks checks
        String username = "", password = "";

        for (String arg :
                args) {
            String[] argParts = arg.split("=");
            switch (argParts[0]) {
                case "username" :
                    username = argParts[1];
                    break;
                case "password" :
                    password = argParts[1];
                    break;
                default :
                    return Parameters.loginErrorInvalidArguments;
            }
        }

        // check against the db
        // TODO : add overloading for getTableElements with filters and select options
        List<PlayersTableRow> players = DatabaseHandler.getTableElements(Players);
        boolean isValid = players != null && players.size() > 0;

        // returns if invalid
        if (!isValid)
            return Parameters.loginErrorInvalidCredentials;

        // generating authentication key
        String authKey = addActiveUser(username);
        if (authKey.startsWith("Error :"))
            return authKey;

        return "OK : " + authKey;
    }

    /**
     * adds the {@code username} to the active users list with his authentication key as the key
     * @param username the username to add to the active users list
     * @return authentication key or error message (starts with "Error")
     */
    private static String addActiveUser(String username) {
        String error = null;
        String authKey = generateAuthKey();

        activeUsers.put(authKey, username);

        if (error != null)
            return "Error :" + error;
        else if (authKey == null)
            return Parameters.loginErrorAuthKeyGeneration;

        return authKey;
    }

    /**
     * generates a unique authentication key
     * @return a unique authentication key
     */
    private static String generateAuthKey() {
        Random rand = new Random();
        String authKey;
        do {
            authKey = "";
            for (int i = 0; i < Parameters.authKeyLength; i++)
                authKey += Parameters.authKeyChars.charAt(rand.nextInt(Parameters.authKeyChars.length()));
        }
        while (getUsernameByKey(authKey) != null);

        return authKey;
    }

    /**
     * returns the username matches the {@code authKey} supplied, null if there isn't one
     * @param authKey the authentication key to check
     * @return username or null
     */
    public static String getUsernameByKey(String authKey) {
        return activeUsers.get(authKey);
    }
}
