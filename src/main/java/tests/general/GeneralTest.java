package tests.general;

import interface_objects.LoginHandler;
import interface_objects.Parser;
import managers.ItemManager;
import objects.Parameters;
import org.junit.Test;
import processes.DatabaseClient;
import processes.Worker;

import java.util.Scanner;

public class GeneralTest {

    public static void main(String[] args){
        try {
            new GeneralTest().operateGame();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

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
        // testing....
        ItemManager.init();

        String userInput =  "init\n";
        userInput += "connect first.bank.cash\n";
        userInput += "help\n";
        userInput += "help.comm\n";
        userInput += "help.commands\n";
        userInput += "system\n";
        userInput += "system.help\n";
        userInput += "system.spec\n";
        userInput += "connect\n";
        userInput += "connect {}\n";
        userInput += "connect {domain:}\n";
        userInput += "connect {domain:test}\n";
        userInput += "connect {domain:first.bank.cash}\n";
        userInput += "connect {doma:first.bank.cash}\n";
        userInput += "exit\n";

        Scanner scanner = new Scanner(userInput);
        String authKey = "";
        String input = "start";
        while (!input.equals("exit")) {
            if (input.equals("start")) {
                String ip = "";
                String temp = LoginHandler.checkLogin("8:5=username:ofirl&8:4=password:test&8:" + ip.length() + "=clientIp:" + ip);
                authKey = temp.substring(5);
            }
            else if (input.equals("init")) {
                String initCommand = "init " + Parameters.InitCommandAutoCompleteList;
                input = "7:32=authKey:" + authKey;
                input += "&4:" + initCommand.length() + "=init:" + initCommand;
                System.out.println(Parser.requestResponse(input));

                initCommand = "init " + Parameters.InitCommandAccountBalance;
                input = "7:32=authKey:" + authKey;
                input += "&4:" + initCommand.length() + "=init:" + initCommand;
                System.out.println(Parser.requestResponse(input));

                initCommand = "init " + Parameters.InitCommandMacros;
                input = "7:32=authKey:" + authKey;
                input += "&4:" + initCommand.length() + "=init:" + initCommand;
                System.out.println(Parser.requestResponse(input));

                initCommand = "init " + Parameters.InitCommandSystemSpec;
                input = "7:32=authKey:" + authKey;
                input += "&4:" + initCommand.length() + "=init:" + initCommand;
                System.out.println(Parser.requestResponse(input));

                initCommand = "init " + Parameters.InitCommandSystemStatus;
                input = "7:32=authKey:" + authKey;
                input += "&4:" + initCommand.length() + "=init:" + initCommand;
                //System.out.println(Parser.requestResponse(input));
            }
            else {
                input = "7:" + input.length() + "=command:" + input;
                input += "&7:32=authKey:" + authKey;

                System.out.println(Parser.requestResponse(input));
            }

            input = scanner.nextLine();
        }

        test1.stop();
        test2.stop();
    }
}
