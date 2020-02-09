package com.smile.read;

import com.smile.write.WriteLock;
import entity.IbftTransaction;
import pool.ConnectionPool;
import utils.Log;
import utils.StopWatch;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReadDataThread extends Thread {

    private final String QUERY_SQL = "SELECT * FROM ibft_transaction WHERE transaction_id >= %s and transaction_id < %s;";

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

        StopWatch sw = new StopWatch();
        sw.start();

        Log.logStart("READ_DATA", String.format("read from: %s, pageSize: %s", this.offset, this.pageSize));
        if (offset == 0 && pageSize == 0) {
            return;
        }
        ConnectionPool pool = null;
        Connection conn = null;
        try {
            pool = ConnectionPool.getInstance();
            conn = pool.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (conn == null) {
            return;
        }

        List<IbftTransaction> data = new ArrayList<>();
        try {
            Statement stm = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stm.setFetchSize(10000);
            ResultSet rs = stm.executeQuery(String.format(QUERY_SQL, offset, offset + pageSize));
            IbftTransaction ent = null;
            while (rs.next()) {
                ent = new IbftTransaction();
                ent.transactionId = rs.getInt(1);
                ent.userId = rs.getInt(2);
                ent.amount = rs.getInt(3);
                ent.status = rs.getShort(4);

                data.add(ent);
            }

            rs.close();
            DataReadQueue dataReadQueue = DataReadQueue.getInstance();
            dataReadQueue.push(data);

            WriteLock writeLock = WriteLock.getInstance();
            writeLock.increaseReadThreadFinsh();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            pool.releaseConection(conn);
            Log.infoEnd("READ_DATA", "Size: " + data.size(),sw.end());
        }
    }
}
