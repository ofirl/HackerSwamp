package interface_objects;

import database_objects.PlayersTableRow;
import objects.ActiveUser;
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
    /**
     * a static field containing all the currently active users
     */
    public static ConcurrentHashMap<String, ActiveUser> activeUsers = new ConcurrentHashMap<>();

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
        PlayersTableRow dbRow = authenticateUser(username, password);

        // returns if invalid
        if (dbRow == null)
            return Parameters.loginErrorInvalidCredentials;

        // generating authentication key
        String authKey = addActiveUser(username, dbRow.id);
        if (authKey.startsWith("Error :"))
            return authKey;

        return "OK : " + authKey;
    }

    /**
     * adds the {@code username} to the active users list with his authentication key as the key
     * @param username the username to add to the active users list
     * @return authentication key or error message (starts with "Error")
     */
    private static String addActiveUser(String username, int id) {
        String error = null;
        String authKey = generateAuthKey();

        activeUsers.put(authKey, new ActiveUser(authKey, username, id, "localhost"));

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
        int attempts = 0;
        do {
            authKey = "";
            attempts++;
            for (int i = 0; i < Parameters.authKeyLength; i++)
                authKey += Parameters.authKeyChars.charAt(rand.nextInt(Parameters.authKeyChars.length()));
        }
        while (getUsernameByKey(authKey) != null && attempts < Parameters.authKeyGenerationMaxAttempts);

        return authKey;
    }

    /**
     * gets the username matches the {@code authKey} supplied, null if there isn't one
     * @param authKey the authentication key to check
     * @return username or null
     */
    public static String getUsernameByKey(String authKey) {
        return activeUsers.get(authKey).username;
    }

    /**
     * gets the {@code ActiveUser} matches the {@code authKey} supplied, null if there isn't one
     * @param authKey the authentication key to check
     * @return ActiveUser object or null
     */
    public static ActiveUser getActiveUserByKey(String authKey) {
        return activeUsers.get(authKey);
    }

    /**
     * checks whether a user with the provided {@code username} and {@code password} exists
     * @param username username to check
     * @param password password to check
     * @return boolean whether the user exists, false if there was an error
     */
    public static PlayersTableRow authenticateUser(String username, String password) {
        List<PlayersTableRow> rows = DatabaseHandler.getTableElements(Players, "username=" + username + " AND password=" + password);
        if (rows == null || rows.size() == 0)
            return null;

        return rows.get(0);
    }
}
