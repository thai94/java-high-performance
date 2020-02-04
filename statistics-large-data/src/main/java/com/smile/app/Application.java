package com.smile.app;

import com.smile.*;
import entity.StatisticsIbft;
import mysql.ConnectionPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Application {

    private static final int MAX_RECORD_PER_THREAD = 5000;


    public static void main(String args[]) throws SQLException, ClassNotFoundException, InterruptedException {

        // read data
        ConnectionPool connPool = null;
        Connection conn = null;
        int numThread = 0;

        try {
            connPool = ConnectionPool.getInstance();
            conn = connPool.getConnection();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT COUNT(*) FROM statistics_large_data.ibft_transaction");
            rs.next();
            Long count = rs.getLong(1);

            numThread = (int) (count / MAX_RECORD_PER_THREAD);
            if (count % MAX_RECORD_PER_THREAD != 0) {
                numThread++;
            }

            ThreadPool readPool = new ThreadPool();
            ReadDataThread readDataThread = null;
            int offset = 0;
            int pageSize = MAX_RECORD_PER_THREAD - 1;
            for (int i = 0; i < numThread; i++) {
                offset = i * MAX_RECORD_PER_THREAD;
                readDataThread = new ReadDataThread(offset, pageSize);
                readPool.add(readDataThread);
            }
            readPool.execute();
        } finally {
            connPool.releaseConection(conn);
        }

        // statictis data
        ThreadPool statictisPool = new ThreadPool();
        ProcessDataThread processDataThread = null;
        for (int i = 0; i < numThread; i++) {
            processDataThread = new ProcessDataThread();
            statictisPool.add(processDataThread);
        }
        statictisPool.execute();

        // merge statictis data
        MergeStatisticThread mergeStatisticThread = new MergeStatisticThread();
        mergeStatisticThread.start();

        // write data
        DataWriteQueue dataWriteQueue = DataWriteQueue.getInstance();
        int writeSize = dataWriteQueue.getSize();
        numThread = (int) (writeSize / MAX_RECORD_PER_THREAD);
        if (writeSize % MAX_RECORD_PER_THREAD != 0) {
            numThread++;
        }
        ThreadPool writePool = new ThreadPool();
        WriteDataThread writeDataThread = null;
        List<StatisticsIbft> writeDataList = null;
        StatisticsIbft writeDataTmp = null;
        for (int i = 0; i < numThread; i++) {
            writeDataList = new ArrayList<>();
            do {
                if (dataWriteQueue.getCount() == dataWriteQueue.getSize() && dataWriteQueue.getPoolSize() == 0) {
                    break;
                }
                writeDataTmp = dataWriteQueue.pool();
                writeDataList.add(writeDataTmp);
            } while (writeDataList.size() != MAX_RECORD_PER_THREAD);
            writeDataThread = new WriteDataThread(writeDataList);
            writePool.add(writeDataThread);
        }
        writePool.execute();
    }
}
