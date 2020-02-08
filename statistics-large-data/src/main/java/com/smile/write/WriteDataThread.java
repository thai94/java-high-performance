package com.smile.write;

import entity.StatisticsIbft;
import pool.ConnectionPool;
import utils.Log;
import utils.StopWatch;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public class WriteDataThread extends Thread {

    private final String INSERT_SQL = "INSERT INTO statistics_ibft(user_id, total, total_sucess, total_amount) VALUES ";
    private final int BATCH_SZIE = 5000;

    private List<StatisticsIbft> writeData;

    public WriteDataThread(List<StatisticsIbft> writeData) {
        this.writeData = writeData;
    }

    @Override
    public void run() {

        StopWatch sw = new StopWatch();
        sw.start();
        Log.logStart("WRITE_DATA", "Size: " + this.writeData.size());

        if (writeData.size() == 0) {
            return;
        }

        int size = writeData.size();
        StringBuilder insertDataBuilder = new StringBuilder(INSERT_SQL);
        StatisticsIbft processItem = null;

        ConnectionPool pool = null;
        Connection conn = null;
        try {
            pool = ConnectionPool.getInstance();
            conn = pool.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            for (int i = 0; i < size; i++) {
                processItem = writeData.get(i);
                insertDataBuilder.append(String.format("(%s, %s, %s, %s)", processItem.userId, processItem.total, processItem.totalSucess, processItem.totalAmount));

                if (i % BATCH_SZIE == 0 || i == size - 1) {

                    Statement stm = conn.createStatement();
                    stm.executeUpdate(insertDataBuilder.toString());
                    insertDataBuilder = new StringBuilder(INSERT_SQL);
                } else {
                    insertDataBuilder.append(",");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            pool.releaseConection(conn);
        }
        Log.infoEnd("WRITE_DATA", sw.end());
    }
}
