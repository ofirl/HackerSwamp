package player_scripts.untrusted;

public class TestScript implements Runnable{

    static {
        System.out.println("hello");
    }

    public TestScript() {
        System.out.println("world");
    }

    public void run() {
        System.out.println("finished running!");
    }
}
