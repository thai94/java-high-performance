package mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ConnectionPool {
    int size = 10;
    Queue<Connection> pool = new LinkedList<>();

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

    ;



    public static ConnectionPool getInstance() throws SQLException, ClassNotFoundException {
        return instance;
    }



    private ConnectionPool() throws SQLException, ClassNotFoundException {

        Class.forName("com.mysql.jdbc.Driver");

        Connection con = null;
        for (int i = 0; i < size; i++) {
            con = DriverManager.getConnection(
                    "jdbc:mysql://10.0.0.213:3306/statistics_large_data","admin","85q2steF$$$");
            pool.add(con);
        }
    }


    public synchronized Connection getConnection() throws InterruptedException {
        if(pool.size() == 0) {
            wait();
        }
        return pool.poll();
    }

    public synchronized void releaseConection(Connection connection) {
        if(connection == null) {
            return;
        }
        pool.add(connection);
        if(pool.size() == 1) {
            notify();
        }
    }

    public int getRemainConection() {
        return this.pool.size();
    }


}
