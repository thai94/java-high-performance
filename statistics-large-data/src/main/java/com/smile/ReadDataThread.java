package com.smile;

import entity.IbftTransaction;
import mysql.ConnectionPool;
import utils.StopWatch;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReadDataThread extends Thread {

    private final String QUERY_SQL = "SELECT * FROM statistics_large_data.ibft_transaction LIMIT %s,%s;";

    int offset = 0;
    int pageSize = 0;

    private ReadDataThread() {
    }

    public ReadDataThread(int offset, int pageSize) {
        this.offset = offset;
        this.pageSize = pageSize;
    }

    @Override
    public void run() {

        if (offset == 0 && pageSize == 0) {
            return;
        }

        StopWatch sw = new StopWatch();
        sw.start();
        ConnectionPool pool = null;
        Connection conn = null;

        try {
            pool = ConnectionPool.getInstance();
            conn = pool.getConnection();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(String.format(QUERY_SQL, offset, pageSize));
            IbftTransaction ent = null;
            List<IbftTransaction> data = new ArrayList<>();
            while (rs.next()) {
                ent = new IbftTransaction();
                ent.transactionId = rs.getInt(1);
                ent.userId = rs.getInt(2);
                ent.amount = rs.getInt(3);
                ent.status = rs.getShort(4);

                data.add(ent);
            }

            DataReadQueue.getInstance().push(data);

            WriteLock writeLock = WriteLock.getInstance();
            writeLock.increaseReadThreadFinsh();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            pool.releaseConection(conn);
        }

    }
}
