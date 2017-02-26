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
    // region login

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

    /**
     * error for locked user
     */
    public static String loginErrorLockedUser = "Error : the user is locked";

    // endregion

    // endregion

    // region parser

        // region error messages

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

    // endregion

    // region worker

        // region error messages

    /**
     * max worker threads allowed simultaneously
     */
    public static int maxWorkerThreads = 5;

    /**
     * error for arguments list parenthesis mismatch '{' and '}'
     */
    public static String SyntaxErrorArgumentsParenthesisMismatch = "Error : syntax error, '{' and '}' mismatch";

    /**
     * error for invalid argument syntax
     */
    public static String SyntaxErrorInvalidArgumentSyntax = "Error : syntax error, could not parse arguments";

    /**
     * error could not find a command prefix (while going over the command list)
     */
    public static String ErrorCommandDoesNotExistsPrefix = "Error : could not find command ";

    /**
     * error could not find a command prefix (while going over the command list)
     */
    public static String ErrorCommandDoesNotExists = "Error : command does not exists";

    /**
     * error could not find the macro
     */
    public static String ErrorMacroNotFound = "Error : macro is not defined";

    /**
     * error for syscmd specs not found
     */
    public static String ErrorSystemSpecsNotFound = "Error : could not find system specs";

    /**
     * error for motherboard not found in syscmd specs
     */
    public static String ErrorSystemSpecMotherboardNotFound = "Error : could not find a motherboard in system specs";

    /**
     * error for cpu not found in syscmd specs
     */
    public static String ErrorSystemSpecCpuNotFound = "Error : could not find a cpu in system specs";

    /**
     * error for ram not found in syscmd specs
     */
    public static String ErrorSystemSpecRamNotFound = "Error : could not find a ram in system specs";

    /**
     * error for hdd not found in syscmd specs
     */
    public static String ErrorSystemSpecHddNotFound = "Error : could not find a hdd in system specs";

    /**
     * error for network card not found in syscmd specs
     */
    public static String ErrorSystemSpecNetworkCardNotFound = "Error : could not find a network card in system specs";

    /**
     * error for invalid init command
     */
    public static String ErrorInvalidInitCommand = "Error : invalid init command";

    /**
     * error for unknown error
     */
    public static String ErrorUnknownError = "Error : unknown error occurred";

    /**
     * error for receiving a null command
     */
    public static String ErrorNullCommand = "Error : didn't receive command";

    // endregion

        // region init commands

    /**
     * the init command template, command of this kind is special and will be received bon initialization of client
     */
    public static String InitCommandTemplate = "init:true";

    /**
     * name of the init parameter for requesting the auto complete list
     */
    public static String InitCommandAutoCompleteList = "autocomplete";

    /**
     * name of the init parameter for requesting the syscmd specs
     */
    public static String InitCommandSystemSpec = "systemspec";

    /**
     * name of the init parameter for requesting the account balance information
     */
    public static String InitCommandAccountBalance = "accountbalance";

    /**
     * name of the init parameter for requesting the syscmd status
     */
    public static String InitCommandSystemStatus = "systemstatus";

    /**
     * name of the init parameter for requesting the macros list
     */
    public static String InitCommandMacros = "macros";

    // endregion

    // endregion

    // region commands

    // region command names

    /**
     * help
     */
    public static String CommandNameHelp = "help";

    /**
     * help.commands
     */
    public static String CommandNameHelpCommands = "commands";

    /**
     * connect
     */
    public static String CommandNameConnect = "connect";

    /**
     * syscmd
     */
    public static String CommandNameSystem = "system";

    /**
     * syscmd.spec
     */
    public static String CommandNameSystemSpec = "spec";

    /**
     * market
     */
    public static String CommandNameMarket = "market";

    /**
     * market.items
     */
    public static String CommandNameMarketItems = "items";

    /**
     * market.scripts
     */
    public static String CommandNameMarketScripts = "scripts";

    // endregion

    // region command errors

    /**
     * error for arguments not in accepted arguments list
     */
    public static String ErrorCommandInvalidArguments = "Error : command includes invalid arguments";

    // endregion

    // endregion

    // region bank

    /**
     * prefix for account not found error
     */
    public static String BankErrorCannotFindAccountPrefix = "Error : cannot find account ";

    /**
     * withdraw account is null error
     */
    public static String BankErrorWithdrawAccountNull = "Error : account to withdraw from is null";

    /**
     * transfer account is null error
     */
    public static String BankErrorTransferAccountNull = "Error : account to transfer to is null";

    // endregion

    // region location names

    /**
     * localhost location name
     */
    public static String LocationNameLocalHost = "localhost";

    // endregion

    // region domain

    /**
     * suffix for public area of domain
     */
    public static String DomainPublicSuffix = "public";

    /**
     * suffix for members area of domain
     */
    public static String DomainMemberSuffix = "members";

    /**
     * suffix for private area of domain
     */
    public static String DomainPrivateSuffix = "private";

    /**
     * suffix for admin area of domain
     */
    public static String DomainAdminSuffix = "admin";

    /**
     * error prefix for domain not found
     */
    public static String ErrorDomainNotFoundPrefix = "Error: cannot find domain ";

    /**
     * message for successfully connecting to a domain
     */
    public static String DomainConnectedSuccessfully = "Connected successfully";

    /**
     * error for non existing active user
     */
    public static String ErrorActiveUserNotFound = "Error : active user can't be found";

    /**
     * error for problems with init
     */
    public static String ErrorDomainsInit = "Error : domains init has encountered in an error (prob. database related)";

    /**
     * main bank name
     */
    public static String MainBankName = "First International Bank";

    // endregion

    // region player context

    /**
     * default location for player context
     */
    public static String DefaultLocation = "localhost";

    // endregion

    // region item manager

    // region error messages

    /**
     * script not found error
     */
    public static String ErrorScriptNotFound = "Error : script not found in market scripts";

    /**
     * main account not found error
     */
    public static String ErrorMainAccountNotFound = "Error : could not find main account";

    /**
     * insufficient funds error
     */
    public static String ErrorInsufficientFunds = "Error : insufficient funds";

    // endregion

    // endregion
}
