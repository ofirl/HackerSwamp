package objects;

import java.sql.ResultSet;

public class DatabaseQuery {
    public String query;
    public int id;
    public ResultSet result;
    public String error;

    public DatabaseQuery(String query, int id) {
        this.query = query;
        this.id = id;
    }
}
