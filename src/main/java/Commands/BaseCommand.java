package Commands;

import com.sun.org.apache.xpath.internal.Arg;
import objects.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class BaseCommand {
    // public variables
    public CommandContext context;
    public String mainName;
    public HashMap<String, Argument> args;
    public static Command superCommand;
    public static List<Argument> acceptedArguments = new ArrayList<>();

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
     * the main command to run
     * @return a response
     */
    public String main() {
        return getHelp();
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
    public String execute(CommandContext context, String subCommand, List<Argument> args) {
        BaseCommand instance = createInstance(context);
        instance.parseArguments(args);
        return instance.call(subCommand);
    }

    /**
     * gets a list of all the sub commands formatted in a string
     * @return a list of sub commands formatted in a string
     */
    public String getSubCommands() {
        if (superCommand == null)
            return "";

        String output = "";
        for (String sub :
                superCommand.subCommands.keySet())
            output += superCommand.name + "." + sub + "\n";

        return output;
    }

    /**
     * adds the supplied {@code arguments} to the {@code args} {@link HashMap}
     * @param arguments the argument list to parse
     */
    public void parseArguments(List<Argument> arguments) {
        args = new HashMap<>();
        arguments.forEach((a) -> args.put(a.name, a));
    }

    public String getHelp() {
        return getSubCommands();
    }
}
