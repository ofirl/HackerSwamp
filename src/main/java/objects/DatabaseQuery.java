package objects;

import java.sql.ResultSet;

public class DatabaseQuery {
    public String query;
    public int id;
    public boolean responseNeeded;
    public ResultSet result;
    public String error;

    public DatabaseQuery(String query, int id, boolean responseNeeded) {
        this.query = query;
        this.id = id;
        this.responseNeeded = responseNeeded;
    }
}
