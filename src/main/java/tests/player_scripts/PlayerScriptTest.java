package player_scripts;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Ofir on 2/11/2017.
 */
public class PlayerScriptTest {
    @Test
    public void createInstance() throws Exception {

    }

    @Test
    public void loadFile() throws Exception {

    }

    @Test
    public void execute() throws Exception {
        PlayerScript test = new PlayerScript();
        test.loadFile("");
        test.execute(null, null, null);
    }

}