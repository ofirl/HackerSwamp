package player_scripts;

import commands.BaseCommand;
import objects.Argument;
import objects.CommandContext;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class PlayerScript extends BaseCommand{

    public File root;
    public File script;

    public PlayerScript() {
        this(null);
    }

    public PlayerScript(CommandContext context) {
        super(context, null);
    }

    public BaseCommand createInstance(CommandContext context) {
        return new PlayerScript(context);
    }

    public void loadFile(String name) {
        root = new File("/target/classes");
        script = new File(root, "player_scripts.untrusted.TestScript.class");

        //JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        //compiler.run(null, null, null, script.getPath());
    }

    // TODO : check SecurityManager

    /**
     * executes a player made script by instancing it and calling it's {@code run()} function
     * @param context the context in which to run the command
     * @param subCommand the sub command to run (or the name of the class to run the main function)
     * @param args arguments for the command
     * @return a response
     */
    @Override
    public String execute(CommandContext context, String subCommand, List<Argument> args) {
        // TODO : run in a different thread, enforce a time limit

        try {
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{root.toURI().toURL()});
            Class<?> cls = Class.forName("player_scripts.untrusted.TestScript", true, classLoader); // Should print "hello".
            Runnable instance = (Runnable) cls.newInstance(); // Should print "world".
            instance.run();
            System.out.println(instance); // Should print "test.Test@hashcode".
        }
        catch (Exception e) {
            // TODO : send the error message as the response
        }

        return null;
    }
/*
    // Prepare source somehow.
    String source = "package test; public class Test { static { System.out.println(\"hello\"); } public Test() { System.out.println(\"world\"); } }";

    // Save source in .java file.
    File root = new File("/java"); // On Windows running on C:\, this is C:\java.
    File sourceFile = new File(root, "test/Test.java");
sourceFile.getParentFile().mkdirs();
Files.write(sourceFile.toPath(), source.getBytes(StandardCharsets.UTF_8));

    // Compile source file.
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
compiler.run(null, null, null, sourceFile.getPath());

    // Load and instantiate compiled class.
    URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { root.toURI().toURL() });
    Class<?> cls = Class.forName("test.Test", true, classLoader); // Should print "hello".
    Object instance = cls.newInstance(); // Should print "world".
System.out.println(instance); // Should print "test.Test@hashcode".
*/
}
