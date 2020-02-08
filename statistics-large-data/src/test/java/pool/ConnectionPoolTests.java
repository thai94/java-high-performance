package pool;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPoolTests {

    @Test
    public void testInitConectionPool() throws SQLException, ClassNotFoundException {
        ConnectionPool pool = ConnectionPool.getInstance();
        Assert.assertEquals(true, pool != null);
    }

    @Test
    public void testObtainConnection() throws SQLException, ClassNotFoundException, InterruptedException {
        ConnectionPool pool = ConnectionPool.getInstance();
        pool.getConnection();
        int remainConnSize = pool.getRemainConection();
        Assert.assertEquals(9, remainConnSize);
    }

    @Test
    public void testReleaseConnection() throws SQLException, ClassNotFoundException, InterruptedException {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection conn = pool.getConnection();
        int remainConnSize = pool.getRemainConection();
        Assert.assertEquals(9, remainConnSize);
        pool.releaseConection(conn);
        remainConnSize = pool.getRemainConection();
        Assert.assertEquals(10, remainConnSize);
    }

    @Test
    public void testWaitConnection() throws SQLException, ClassNotFoundException, InterruptedException {
        ConnectionPool pool = ConnectionPool.getInstance();
        for (int i = 0; i < 11; i++) {
            pool.getConnection();
        }
    }
}
