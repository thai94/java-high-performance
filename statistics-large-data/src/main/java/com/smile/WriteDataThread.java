package com.smile;

import entity.StatisticsIbft;
import mysql.ConnectionPool;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public class WriteDataThread extends Thread {

    private final String INSERT_SQL = "INSERT INTO statistics_large_data.StatisticsIbft(userId, total, totalSucess, totalAmount) VALUES ";
    private final int BATCH_SZIE = 5000;

    private List<StatisticsIbft> writeData;

    public WriteDataThread(List<StatisticsIbft> writeData) {
        this.writeData = writeData;
    }

    @Override
    public void run() {

        if (writeData.size() == 0) {
            return;
        }

        int size = writeData.size();
        StringBuilder insertDataBuilder = new StringBuilder(INSERT_SQL);
        StatisticsIbft processItem = null;

        ConnectionPool pool = null;
        Connection conn = null;

        for (int i = 0; i < size; i++) {
            processItem = writeData.get(i);
            insertDataBuilder.append(String.format("(%s, %s, %s, %s)", processItem.userId, processItem.total, processItem.totalSucess, processItem.totalAmount));

            if (i % BATCH_SZIE == 0 || i == size - 1) {
                try {
                    pool = ConnectionPool.getInstance();
                    conn = pool.getConnection();
                    Statement stm = conn.createStatement();
                    stm.executeUpdate(insertDataBuilder.toString());
                    insertDataBuilder = new StringBuilder(INSERT_SQL);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                insertDataBuilder.append(",");
            }
        }
    }
}
