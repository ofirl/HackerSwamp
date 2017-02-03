package interface_objects;

import database_objects.*;
import objects.DatabaseQuery;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedTransferQueue;
import java.util.function.Supplier;

enum Tables {
    Players, Accounts, Corps, Domains, Inventories, Macros,
    Autocomplete, Commands,
    Marketitems, Motherboards, Cpus, Rams, Hdds, Networkcards
}

public class DatabaseHandler {

    // region sql queries handling

    /**
     * queries request queue
     */
    public static LinkedTransferQueue<DatabaseQuery> queryQueue = new LinkedTransferQueue<>();

    /**
     * queries response queue
     */
    public static ConcurrentHashMap<Integer, DatabaseQuery> resultQueue = new ConcurrentHashMap<>();

    public static int currentId = 0;

    /**
     * transfers {@code c} to a worker
     * @param query the element to enqueue
     */
    public static void transferQuery(DatabaseQuery query) {
        try {
            queryQueue.transfer(query);
        }
        catch (Exception e) { }
    }

    /**
     * receives a query from the queue, blocking
     * @return the top query from the queue
     */
    public static DatabaseQuery receiveQuery() {
        DatabaseQuery output = null;
        try {
            output = queryQueue.take();
        }
        catch (Exception e) { }

        return output;
    }

    /**
     * safely enqueues {@code c} to {@link #queryQueue}
     * @param query the element to enqueue
     */
    public static void responseEnqueue(DatabaseQuery query) {
        resultQueue.put(query.id ,query);
    }

    /**
     * add a response and notify the relevant object
     * @param key the key to add the response to
     * @param response the response
     */
    public static void addResponse(int key, ResultSet response) {
        synchronized (resultQueue.get(key)) {
            DatabaseQuery responseCommandRequest = resultQueue.get(key);
            responseCommandRequest.result = response;
            responseCommandRequest.notify();
        }
    }

    /**
     * safely dequeues an element from {@link #queryQueue}
     * @return the top element in {@link #queryQueue} if one exists, null if the queue is empty
     */
    public static DatabaseQuery responseDequeue(int key) {
        return resultQueue.get(key);
    }

    /**
     * returns when response is received for {@code key}
     * @param key the key to wait for response for
     * @return a response matching the key
     */
    public static ResultSet waitForResponse(int key) {
        synchronized (resultQueue.get(key)) {
            DatabaseQuery responseCommandRequest = resultQueue.get(key);
            try {
                while (responseCommandRequest.result == null)
                    responseCommandRequest.wait();
            }
            catch (Exception e) { }

            resultQueue.remove(key);
            return responseCommandRequest.result;
        }
    }

    /**
     * transfers a command to a worker
     * @param query the command to run
     * @return response for the command
     */
    public static void addQuery(DatabaseQuery query) {
        if (query.responseNeeded) {
            responseEnqueue(query);
            transferQuery(query);
        }
        else
            queryQueue.add(query);
    }

    /**
     * executes the {@code input} as a sql query
     * @param input the sql query to execute
     * @return the result of the query
     */
    public static void requestAction(String input) {
        // validity check
        if (input == null || input.equals(""))
            return;

        int queryKey = getNextQueryId();
        DatabaseQuery c = new DatabaseQuery(input, queryKey, false);

        addQuery(c);
    }

    /**
     * executes the {@code input} as a sql query and returns the result
     * @param input the sql query to execute
     * @return the result of the query
     */
    public static ResultSet requestResponse(String input) {
        // validity check
        if (input == null || input.equals(""))
            return null;

        int queryKey = getNextQueryId();
        DatabaseQuery c = new DatabaseQuery(input, queryKey, true);

        addQuery(c);
        return waitForResponse(queryKey);
    }

    /**
     * gets the next query id
     * @return the next query id
     */
    public static int getNextQueryId() {
        return currentId++;
    }

    // endregion

    // region table handling

    /**
     * static field to save a mapping between types and their classes (for instantiation)
     */
    public static HashMap<Tables, Class> elementTypes = new HashMap<>();
    // elementTypes initialization
    static {
        elementTypes.put(Tables.Accounts, AccountsTableRow.class);
        elementTypes.put(Tables.Autocomplete, AutocompleteTableRow.class);
        elementTypes.put(Tables.Commands, CommandsTableRow.class);
        elementTypes.put(Tables.Corps, CorpsTableRow.class);
        elementTypes.put(Tables.Cpus, CpusTableRow.class);
        elementTypes.put(Tables.Domains, DomainsTableRow.class);
        elementTypes.put(Tables.Hdds, HddsTableRow.class);
        elementTypes.put(Tables.Inventories, InventoriesTableRow.class);
        elementTypes.put(Tables.Macros, MacrosTableRow.class);
        elementTypes.put(Tables.Marketitems, MarketitemsTableRow.class);
        elementTypes.put(Tables.Motherboards, MotherboardsTableRow.class);
        elementTypes.put(Tables.Networkcards, NetworkcardsTableRow.class);
        elementTypes.put(Tables.Players, PlayersTableRow.class);
        elementTypes.put(Tables.Rams, RamsTableRow.class);
    }

    /**
     * gets the columns of the result set provided
     * @param rs the result set to get the columns for
     * @return a {@link HashMap} of the form {@code HashMap<columnName, columnType>}
     * when {@code columnType} is a number from java.sql.Types
     */
    public static HashMap<String, Integer> getResultSetColumns(ResultSet rs) {
        HashMap<String, Integer> columns = new HashMap<>();
        try {
            ResultSetMetaData meta = rs.getMetaData();
            for (int i = 0; i < meta.getColumnCount(); i++) {
                columns.put(meta.getColumnName(i), meta.getColumnType(i));
            }

            return columns;
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * formats the filter array provided to SQL friendly format
     * @param filters the filters array to format
     * @param relations the relations between the filters
     * @return the {@code filters} & {@code relations} formatted
     */
    public static String formatFilters(String[] filters, String[] relations) {
        // sanity check
        if (filters.length != relations.length + 1)
            return null;

        String filtersFormatted = "";
        for (int i = 0; i < relations.length; i++)
            filtersFormatted += filters[i] + relations[i];

        filtersFormatted += filters[filters.length - 1];

        return filtersFormatted;
    }

    /**
     * gets all table elements,
     * same as {@link #getTableElements(Tables, String, String)} with null as columns and filters
     * @param table the table to select from
     * @param <T> the element type to cast to (and return)
     * @return the elements in the table assigned as {@code T}
     */
    public static <T> List<T> getTableElements(Tables table) {
        return getTableElements(table, null);
    }

    /**
     * gets the table elements, selecting only certain columns
     * same as {@link #getTableElements(Tables, String, String)} with null as filters
     * @param table the table to select from
     * @param columns the columns to select
     * @param <T> the element type to cast to (and return)
     * @return the elements in the table assigned as {@code T}
     */
    public static <T> List<T> getTableElements(Tables table, String columns) {
        return getTableElements(table, columns, null);
    }

    /**
     * gets the table elements
     * @param table the table to select from
     * @param columns the columns to select
     * @param filters filters to apply
     * @param relations relations (AND, OR) between the filters
     * @param <T> the element type to cast to (and return)
     * @return the elements in the table assigned as {@code T}
     */
    public static <T> List<T> getTableElements(Tables table, String columns, String[] filters, String[] relations) {
        return getTableElements(table, columns, formatFilters(filters, relations));
    }

    /**
     * gets the table elements
     * @param table the table to select from
     * @param columns the columns to select
     * @param filter filters to apply
     * @param <T> the element type to cast to (and return)
     * @return the elements in the table assigned as {@code T}
     */
    // hate to do this... but i have to...
    @SuppressWarnings("unchecked")
    public static <T> List<T> getTableElements(Tables table, String columns, String filter) {
        // build the query
        String query = "SELECT ";
        query += columns != null ? columns : '*';
        query += " FROM " + table.name();
        query += filter != null ? " WHERE " + filter : "";

        ResultSet rs = requestResponse(query);
        HashMap<String, Integer> rsColumns = getResultSetColumns(rs);
        if (rsColumns == null)
            return null;

        List<T> elements = new ArrayList<>();
        try {
            while (rs.next()) {
                // unchecked warning suppressed
                //T te = (T)elementTypes.get(table).newInstance();
                T ele = ((Class<T>)elementTypes.get(table)).newInstance();
                for (String columnName :
                        rsColumns.keySet()) {
                    ele.getClass().getDeclaredField(columnName).set(ele, rs.getObject(columnName));
                }
                elements.add(ele);
            }

            return elements;
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * checks whether an element exists
     * @param table the table to select from
     * @param filter filters to apply
     * @param <T> the element type to check for
     * @return whether an element exists or not, returns false on error
     */
    public static <T> boolean checkElementsExistence(Tables table, String filter) {
        List<T> elements = getTableElements(table, null, filter);
        return elements != null && elements.size() > 0;
    }

    // endregion
}
