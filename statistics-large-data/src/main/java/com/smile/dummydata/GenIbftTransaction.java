package com.smile.dummydata;

import entity.IbftTransaction;
import mysql.ConnectionPool;
import utils.StopWatch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class GenIbftTransaction extends Thread {

    public static final int MAX_USER = 10000000;
    public static final int MAX_AMOUNT = 10000000;
    public final String INSERT_SQL = "INSERT INTO `statistics_large_data`.`ibft_transaction` (`transaction_id`, `user_id`, `amount`, `status`) VALUES ";
    public final String INSERT_VALUE = "(%s, %s, %s, %s)";
    public final int BATCH_SIZE = 5000;

    int startTranId = 0;
    int endTranId = 0;

    public GenIbftTransaction(int startTranId, int endTranId) {
        this.startTranId = startTranId;
        this.endTranId = endTranId;
    }

    @Override
    public void run() {
        StopWatch sw = new StopWatch();
        sw.start();
        ConnectionPool pool = null;
        Connection conn = null;
        try {
            pool = ConnectionPool.getInstance();
            conn = pool.getConnection();
            Statement stm = conn.createStatement();

            StringBuilder builder = new StringBuilder();
            String sql = "";
            for(int i = startTranId; i <= endTranId; i++) {
                builder.append(String.format(INSERT_VALUE, i, i % MAX_USER, i % MAX_AMOUNT, 1));
                builder.append(",");
                if(i % BATCH_SIZE == 0) {
                    sql = INSERT_SQL + builder.toString();
                    sql = sql.substring(0, sql.length() -1);
                    stm.executeUpdate(sql);
                    builder = new StringBuilder();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.releaseConection(conn);
            sw.end();
        }
    }
}
