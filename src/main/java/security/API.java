package security;

import interface_objects.Parser;

/**
 * API class for untrusted code
 */
public class API {
    public String authKeyArgument;

    public API(String authKey) {
        this.authKeyArgument = Parser.encodeArgument("authKey", authKey);
    }

    public String executeCommand(String command) {
        String commandArgument = Parser.encodeArgument("command", command);
        return Parser.requestResponse(authKeyArgument + "&" + commandArgument);
    }
}
