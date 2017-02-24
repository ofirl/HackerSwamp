package processes;

import java.sql.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

import java.net.URI;
import java.net.URISyntaxException;

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

        get("/db", (req, res) -> {
  /*private void showDatabase(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException { */
            Connection connection = null;
            Map<String, Object> attributes = new HashMap<>();

            try {
                connection = getConnection();

                Statement stmt = connection.createStatement();
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
                stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
                ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

                ArrayList<String> output = new ArrayList<>();
                while (rs.next()) {
                    output.add( "Read from DB: " + rs.getTimestamp("tick"));
                }

                attributes.put("results", output);
                return new ModelAndView(attributes, "db.ftl");
            } catch (Exception e) {
                attributes.put("message", "There was an error: " + e);
                return new ModelAndView(attributes, "error.ftl");
            } finally {
                if (connection != null) try{connection.close();} catch(SQLException e){}
            }
        }, new FreeMarkerEngine());

    }

    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));
        int port = dbUri.getPort();
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + port + dbUri.getPath();

        if (dbUri.getUserInfo() != null) {
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            return DriverManager.getConnection(dbUrl, username, password);
        } else {
            return DriverManager.getConnection(dbUrl);
        }
    }

    private static String decodeUrl(String input) {
        input = input.replaceAll("%3a", ":").replaceAll("%7b", "{").replaceAll("%7d", "}");
        input = input.replaceAll("\\+", " ").replaceAll("%27", "'");
        return input;
    }
}
