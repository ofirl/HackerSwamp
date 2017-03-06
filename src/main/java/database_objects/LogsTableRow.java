package database_objects;

import java.sql.Timestamp;

public class LogsTableRow {
    public int id;
    public Timestamp time;
    public int domain;
    public String entry;
    public int deleted;
}
