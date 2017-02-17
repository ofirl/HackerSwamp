package commands;

public enum CommandAccess {
    /**
     * system command access - can be accessed by everyone, anywhere
     */
    system,
    /**
     * public command access - can be accessed by everyone, anywhere
     */
    Public,
    /**
     * shared command access - can be accessed by organization members and friends
     */
    Shared,
    /**
     * organization command access - can be accessed by organization members
     */
    Organization,
    /**
     * friends command access - can be accessed by friends
     */
    Friends,
    /**
     * private command access - can't be accessed
     */
    Private,
    /**
     * location command access -can be accessed from only certain locations
     */
    Location
}
