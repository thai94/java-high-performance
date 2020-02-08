package pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

public class ConnectionPool {
    public static final String HOST = "10.0.0.200:3306";
    public static final String DB_NAME = "statistics_large_data";
    public static final String USER = "admin";
    public static final String PASSWORD = "12345678";

    public static final int MAX_CONNECTON_POOL_SIZE = 20;


    private static ConnectionPool instance;

    static {
        try {
            instance = new ConnectionPool();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    Queue<Connection> pool = new LinkedList<>();

    private ConnectionPool() throws SQLException, ClassNotFoundException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = String.format("jdbc:mysql://%s/%s", HOST, DB_NAME);

        Connection con = null;
        for (int i = 0; i < MAX_CONNECTON_POOL_SIZE; i++) {
            con = DriverManager.getConnection(url, USER, PASSWORD);
            pool.add(con);
        }
    }

    public static ConnectionPool getInstance() throws SQLException, ClassNotFoundException {
        return instance;
    }

    public synchronized Connection getConnection() throws InterruptedException {
        if (pool.size() == 0) {
            wait();
        }
        return pool.poll();
    }

    public synchronized void releaseConection(Connection connection) {
        if (connection == null) {
            return;
        }
        if (pool.size() == 0) {
            notify();
        }
        pool.add(connection);
    }

    public int getRemainConection() {
        return this.pool.size();
    }
}
