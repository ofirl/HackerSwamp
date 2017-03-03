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

    /**
     * error for invalid macro syntax
     */
    public static String ErrorMacroInvalidSyntax = "Error : invalid set macro syntax";

    /**
     * error for macro could not be set
     */
    public static String ErrorMacroSetFailed = "Error : could not add macro";

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

    /**
     * macro
     */
    public static String CommandNameMacro = "macro";

    /**
     * macro.remove
     */
    public static String CommandNameMacroRemove = "remove";

    /**
     * macro.add
     */
    public static String CommandNameMacroAdd = "add";

    /**
     * macro.view
     */
    public static String CommandNameMacroView = "view";

    /**
     * logs
     */
    public static String CommandNameLogs = "logs";

    /**
     * logs.view
     */
    public static String CommandNameLogsView = "view";

    /**
     * logs.delete
     */
    public static String CommandNameLogsDelete = "delete";

    /**
     * loot
     */
    public static String CommandNameLoot = "loot";

    // endregion

    // region command errors

    /**
     * error for arguments not in accepted arguments list
     */
    public static String ErrorCommandInvalidArguments = "Error : command includes invalid arguments";

    /**
     * error for item does not exists
     */
    public static String ErrorMarketItemNotFound = "Error : could not find item";

    /**
     * error for item already found in inventory
     */
    public static String ErrorMarketItemAlreadyBought = "Error : this item is already in your inventory";

    // endregion

    // region command usages

    /**
     * macro.add usage
     */
    public static String CommandUsageMacroAdd = "Usage : macro.add <macro name> <macro value>";

    /**
     * macro.remove usage
     */
    public static String CommandUsageMacroRemove = "Usage : macro.remove <macro name>";

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

    // region obstacle

    //region obstacle errors

    /**
     * error for not receiving user obstacle status
     */
    public static String ErrorUserStatusNotFound = "Error : could not pull obstacle status for users";

    // endregion

    // endregion

    // region loot

    /**
     * min amount of money in tier 1 money loot
     */
    public static int LootMoneyT1MinAmount = 1000;

    /**
     * max amount of money in tier 1 money loot
     */
    public static int LootMoneyT1MaxAmount = 10000;

    /**
     * chance to get an addition item in t1 loot
     */
    public static double LootItemT1Chance = 0.20;

    // endregion
}
