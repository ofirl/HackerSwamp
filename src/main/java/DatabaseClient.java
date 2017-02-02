import interface_objects.DatabaseHandler;
import objects.DatabaseQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

public class DatabaseClient {
    /**
     * entry point of the database client process
     * @param args
     */
    public static void main(String[] args) {
        DatabaseQuery query;
        while (true) {
            // get a query
            query = null;
            while (query == null)
                query = DatabaseHandler.receiveQuery();

            executeQuery(query);
        }
    }

    /**
     * executed the given {@code dbQuery}
     * @param dbQuery the query to execute
     * @return
     * <p>
     * {@link ResultSet} if query is a select, null otherwise.
     * </p>
     * <p>
     * will not return the error if one occurred,
     * to check for errors check the {@code dbQuery} field {@link DatabaseQuery#error}
     * </p>
     */
    public static ResultSet executeQuery(DatabaseQuery dbQuery) {
        ResultSet rs = null;

        Connection connection = null;
        try {
            connection = getConnection();
            Statement stmt = connection.createStatement();
            if (dbQuery.query.startsWith("SELECT"))
                rs = stmt.executeQuery(dbQuery.query);
            else
                stmt.executeUpdate(dbQuery.query);
        }
        catch (Exception e) {
            dbQuery.error = e.getMessage();
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                }
                catch (SQLException e) {
                    if (dbQuery.error == null)
                        dbQuery.error = "";
                    else
                        dbQuery.error += "\n";

                    dbQuery.error += e.getMessage();
                }
            }
        }

        return rs;
    }

    /**
     * gets a database connection
     * @return a database connection
     * @throws URISyntaxException syntax exception
     * @throws SQLException sql exception
     */
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


    /*
    try {
                connection = getConnection();

                Statement stmt = connection.createStatement();
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
                stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
                ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

                ArrayList<String> output = new ArrayList<String>();
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

     */
}
