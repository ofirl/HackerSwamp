package processes;

import objects.Command;

import java.util.HashMap;

// TODO : add tests for everything

public class WorkerTest {
    @org.junit.Before
    public void setUp() throws Exception {
        //Worker.initializeCommands();
    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    public void main() throws Exception {

    }

    @org.junit.Test
    public void initializeCommands() throws Exception {

    }

    @org.junit.Test
    public void workerStart() throws Exception {

    }

    @org.junit.Test
    public void getAccessibleCommands() throws Exception {

    }

    @org.junit.Test
    public void getAccessibleCommands1() throws Exception {
        HashMap<String, Command> test = Worker.getAllAccessibleCommands(null);
        if (test.size() != 1) throw new AssertionError("test");
    }

    @org.junit.Test
    public void parseCommand() throws Exception {

    }

    @org.junit.Test
    public void checkSyntax() throws Exception {

    }

}