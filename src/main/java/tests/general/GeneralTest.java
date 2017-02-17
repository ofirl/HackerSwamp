package tests.general;

import interface_objects.LoginHandler;
import interface_objects.Parser;
import org.junit.Test;
import processes.DatabaseClient;
import processes.Worker;

import java.util.Scanner;

public class GeneralTest {

    @Test
    public void operateGame() throws Exception{
        Thread test1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Worker.main(null);
            }
        });

        Thread test2 = new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseClient.main(null);
            }
        });

        test1.start();
        test2.start();

        Scanner scanner = new Scanner(System.in);
        String input = "start";
        while (!input.equals("exit")) {
            if (input.equals("start")) {
                LoginHandler.checkLogin("8:5=username:ofirl&8:4=password:test");
                continue;
            }

            Parser.requestResponse(input);

            input = scanner.nextLine();
        }
    }
}
