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

    public static HashMap<Tables, Class> elementTypes = new HashMap<>();
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

    public static <T> T test(Class<T> tst) {
        try {
            return tst.newInstance();
        }
        catch (Exception e) {
            return null;
        }
    }

    // hate to do this... but i have to...
    @SuppressWarnings("unchecked")
    public static <T> List<T> getTableElements(Tables table) {
        ResultSet rs = requestResponse("SELECT * FROM " + table.name());
        HashMap<String, Integer> columns = getResultSetColumns(rs);
        if (columns == null)
            return null;

        List<T> elements = new ArrayList<>();
        try {
            while (rs.next()) {
                T ele = ((Class<T>)elementTypes.get(table)).newInstance();
                for (String columnName :
                        columns.keySet()) {
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

    // endregion
}
