package objects;

/**
 * general class for all the parameters :
 * <ul>
 *     <li> login parameters
 *          <ul>
 *              <li> {@link #authKeyChars} </li>
 *              <li> {@link #authKeyLength} </li>
 *              <li> {@link #authKeyGenerationMaxAttempts} </li>
 *          </ul>
 *      </li>
 *      <li> login error messages
 *         <ul>
 *              <li> {@link #loginErrorArgumentsCount} </li>
 *              <li> {@link #loginErrorArgumentsSyntax} </li>
 *              <li> {@link #loginErrorInvalidArguments} </li>
 *              <li> {@link #loginErrorInvalidCredentials} </li>
 *              <li> {@link #loginErrorAuthKeyGeneration} </li>
 *      </ul>
 *      </li>
 *      <li> worker parameters
 *          <ul>
 *              <li> {@link #maxWorkerThreads} </li>
 *          </ul>
 *      </li>
 * </ul>
 */
public class Parameters {
    // region login parameters

    /**
     * authentication key will be generated from this characters
     */
    public static String authKeyChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * length of generated authentication key
     */
    public static int authKeyLength = 32;

    /**
     * maximum attempts for authentication key generation
     */
    public static int authKeyGenerationMaxAttempts = 10;

    // endregion

    // region login error messages

    /**
     * error for invalid arguments count (not 2 arguments)
     */
    public static String loginErrorArgumentsCount = "Error : invalid arguments count";

    /**
     * error for invalid arguments syntax
     */
    public static String loginErrorArgumentsSyntax = "Error : invalid argument syntax";

    /**
     * error for invalid arguments name
     */
    public static String loginErrorInvalidArguments = "Error : invalid argument name";

    /**
     * error for invalid username - password match
     */
    public static String loginErrorInvalidCredentials = "Error : invalid username or password";

    /**
     * error for authentication key problems
     */
    public static String loginErrorAuthKeyGeneration = "Error : could not generate authentication key";

    // endregion

    // region parser error messages

    /**
     * error for invalid arguments
     */
    public static String parserErrorInvalidArguments = "Error : invalid arguments";

    /**
     * error for no authentication key
     */
    public static String parserErrorNoAuthKey = "Error : no authentication key found";

    /**
     * error for bad authentication key
     */
    public static String parserErrorBadAuthKey = "Error : no active player found for the authentication key";

    // endregion

    // region worker parameters

    /**
     * max worker threads allowed simultaneously
     */
    public static int maxWorkerThreads = 5;

    // endregion
}
