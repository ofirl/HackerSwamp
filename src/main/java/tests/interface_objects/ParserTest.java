package interface_objects;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class ParserTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void transferCommand() throws Exception {

    }

    @Test
    public void receiveCommand() throws Exception {

    }

    @Test
    public void responseEnqueue() throws Exception {

    }

    @Test
    public void addResponse() throws Exception {

    }

    @Test
    public void responseDequeue() throws Exception {

    }

    @Test
    public void waitForResponse() throws Exception {

    }

    @Test
    public void addCommand() throws Exception {

    }

    @Test
    public void requestResponse() throws Exception {

    }

    @Test
    public void encodeArgument() throws Exception {
        String output = Parser.encodeArgument("test", "testing");
        assert output.equals("4:7 test:testing");
    }

    @Test
    public void encodeArgumentList() throws Exception {
        HashMap<String, String> input = new HashMap<>();
        input.put("test", "testing");
        input.put("test2", "testing2");

        String output = Parser.encodeArgumentList(input);
        assert output.equals("5:8 test2:testing2&4:7 test:testing");
    }

    @Test
    public void decodeArgumentsList() throws Exception {
        String input = "7:12 testing:testingValue&2:3 tw:two";
        HashMap<String, String> output = Parser.decodeArgumentsList(input);
        assert output.size() == 2;
        assert output.get("testing").equals("testingValue");
        assert output.get("tw").equals("two");
    }

}