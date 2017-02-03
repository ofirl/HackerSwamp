package Commands;

import objects.*;

public abstract class BaseCommand {
    // public variables
    public CommandContext context;
    public String mainName;
    public static Command superCommand;

    /**
     * constructor
     * @param context the context in which to run
     */
    public BaseCommand(CommandContext context) {
        this.context = context;
    }

    /**
     * creates an instance of the class , will be overridden in the appropriate class
     * @param context the context of the new instance
     * @return a new instance of the appropriate implementation
     */
    public abstract BaseCommand createInstance(CommandContext context);

    /**
     * calling the appropriate function, defined by {@code subCommand}
     * @param subCommand the function to call
     * @return the command result
     */
    public String call(String subCommand) {
        if (subCommand == null || subCommand.equals(mainName))
            subCommand = "main";

        try {
            return (String) getClass().getDeclaredMethod(subCommand).invoke(this);
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * executes the {@code subCommand} in the given context
     * @param context the context in which to run the command
     * @param subCommand the sub command to run (or the name of the class to run the main function)
     * @return the command result
     */
    public String execute(CommandContext context, String subCommand) {
        BaseCommand instance = createInstance(context);
        return instance.call(subCommand);
    }

    /**
     * gets a list of all the sub commands formatted in a string
     * @return a list of sub commands formatted in a string
     */
    public String getSubCommands() {
        String output = "";
        for (String sub :
                superCommand.subCommands.keySet())
            output += superCommand.name + "." + sub + "\n";

        return output;
    }
}
