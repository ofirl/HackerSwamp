package commands;

import objects.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
// check for invalid argument - at the start of "view" function (sub command)
        if (!checkArguments(acceptedArguments.get("view")))
            return Parameters.ErrorCommandInvalidArguments;
 */

public abstract class BaseCommand {
    // public variables
    public CommandContext context;
    public String mainName;
    public HashMap<String, Argument> args;

    /**
     * constructor
     * @param context the context in which to run
     */
    public BaseCommand(CommandContext context, String mainName) {
        this.context = context;
        this.mainName = mainName;
    }

    /**
     * creates an instance of the class , will be overridden in the appropriate class
     * @param context the context of the new instance
     * @return a new instance of the appropriate implementation
     */
    public abstract BaseCommand createInstance(CommandContext context);

    /**
     * the main command to run
     * @return a response
     */
    public String main(Command mainCommand) {
        return getHelp(mainCommand);
    }

    /**
     * calling the appropriate function, defined by {@code subCommand}
     * @param subCommand the function to call
     * @return the command result
     */
    public String call(String subCommand) {
        if (subCommand == null || subCommand.equals(mainName))
            subCommand = "main";

        try {
            // TODO : generalize checkArguments
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
     * @param args arguments for the command
     * @return the command result
     */
    public String execute(CommandContext context, String subCommand, List<Argument> args) {
        BaseCommand instance = createInstance(context);
        instance.parseArguments(args);
        return instance.call(subCommand);
    }

    /**
     * gets a list of all the sub commands formatted in a string
     * @return a list of sub commands formatted in a string
     */
    public String getSubCommands(Command mainCommand) {
        if (mainCommand == null)
            return "";

        String output = "";
        for (String sub :
                mainCommand.subCommands.keySet())
            output += mainCommand.name + "." + sub + "\n";

        return output;
    }

    /**
     * adds the supplied {@code arguments} to the {@code args} {@link HashMap}
     * @param arguments the argument list to parse
     */
    public void parseArguments(List<Argument> arguments) {
        args = new HashMap<>();
        arguments.forEach((a) -> args.put(a.name.trim(), a));
    }

    /**
     * default help method
     * @param mainCommand the main command
     * @return a default help response
     */
    public String getHelp(Command mainCommand) {
        return getSubCommands(mainCommand);
    }

    /**
     * checks arguments names and types are as expected
     * @return whether the arguments are as expected
     */
    public boolean checkArguments(HashMap<String, Argument> acceptedCommandArgs) {
        for (String arg :
                args.keySet()) {
            if (!acceptedCommandArgs.containsKey(arg) || !acceptedCommandArgs.get(arg).type.equals(args.get(arg).type))
                return false;
        }

        return true;
    }
}
