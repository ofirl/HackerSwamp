package processes;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

import interface_objects.LoginHandler;
import interface_objects.Parser;
import spark.template.freemarker.FreeMarkerEngine;
import spark.ModelAndView;

public class WebListener {

    public static void main(String[] args) {

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

        port(Integer.valueOf(System.getenv("PORT")));
        staticFileLocation("/public");

        get("/hello", (req, res) -> {
            return "hi!";
        });

        get("/", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Hello World!");

            return new ModelAndView(attributes, "index.ftl");
        }, new FreeMarkerEngine());


        post("/db", (req, res) -> {
            String input = decodeUrl(req.body());
            return Parser.requestResponse(input);
        });

        post("/login", (req, res) -> {
            String input = decodeUrl(req.body());
            return LoginHandler.checkLogin(input);
        });


        get("/test", (req, res) -> {
  /*private void showDatabase(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException { */

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "test");
            return new ModelAndView(attributes, "message.ftl");
        },new FreeMarkerEngine());
    }

    private static String decodeUrl(String input) {
        try {
            return java.net.URLDecoder.decode(input, "UTF-8");
        }
        catch (Exception e) {
            return "";
        }

        /*
        input = input.replaceAll("%3a", ":").replaceAll("%7b", "{").replaceAll("%7d", "}");
        input = input.replaceAll("\\+", " ").replaceAll("%27", "'");
        return input;
        */
    }

    public static String executePost(String targetURL, String urlParameters) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length",
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
