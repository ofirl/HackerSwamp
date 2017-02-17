package processes;

import interface_objects.DatabaseHandler;
import managers.Logger;
import objects.DatabaseQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.HashMap;

public class DatabaseClient {
    /**
     * entry point of the database client process
     * @param args
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        DatabaseQuery query;
        while (true) {
            // get a query
            query = null;
            Logger.log("DatabaseClient", "waiting for query");
            while (query == null)
                query = DatabaseHandler.receiveQuery();

            Logger.log("DatabaseClient", "got query " + query.query);
            ResultSet rs = executeQuery(query);
            Logger.log("DatabaseClient", "query " + query.query + " got " + rs);
            DatabaseHandler.addResponse(query.id, rs);
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
            addSqlTypes(connection);
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
     * adds the custom sql types to the connection
     * @param con the connection to add sql types to
     */
    public static void addSqlTypes(Connection con) {
        try {
            java.util.Map<String, Class<?>> map = con.getTypeMap();
            if (map == null)
                map = new HashMap<>();

            //Class<?> test = Class.forName("database_objects.CommandAccessSqlType");
            //Logger.log("DatabaseClient.addSqlType", "class found " + test.getName());
            map.put("public.command_access", String.class);
            con.setTypeMap(map);
        }
        catch (Exception e) {
            Logger.log("DatabaseClient.addSqlType", "adding types failed, " + e.getMessage());
            e.printStackTrace();
        }
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
