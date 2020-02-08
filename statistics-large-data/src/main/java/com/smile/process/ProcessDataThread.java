package com.smile.process;

import com.smile.write.WriteLock;
import com.smile.read.DataReadQueue;
import entity.IbftTransaction;
import entity.StatisticsIbft;
import utils.Log;
import utils.StopWatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessDataThread extends Thread {

    @Override
    public void run() {

        StopWatch sw = new StopWatch();
        sw.start();
        Log.logStart("PROCESS_DATA", "");

        List<IbftTransaction> data = new ArrayList<>();
        try {
            DataReadQueue dataReadQueue = DataReadQueue.getInstance();
            data = dataReadQueue.pool();
            if (data == null || data.size() == 0) {
                return;
            }

            Map<Integer, StatisticsIbft> statisticsIbft = new HashMap<>();
            int size = data.size();
            IbftTransaction processingTran = null;
            StatisticsIbft tmpIbftSts = null;
            for (int i = 0; i < size; i++) {
                processingTran = data.get(i);

                if (statisticsIbft.containsKey(processingTran.userId)) {
                    tmpIbftSts = statisticsIbft.get(processingTran.userId);
                } else {
                    tmpIbftSts = new StatisticsIbft();
                }
                tmpIbftSts.userId = processingTran.userId;
                tmpIbftSts.total += 1;
                tmpIbftSts.totalSucess += processingTran.status;
                tmpIbftSts.totalAmount += processingTran.amount;
                statisticsIbft.put(tmpIbftSts.userId, tmpIbftSts);
            }

            StatisticQueue queue = StatisticQueue.getInstance();
            queue.push(statisticsIbft);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            WriteLock writeLock = WriteLock.getInstance();
            writeLock.increaseStatisticThreadFinsh();
        }
        Log.infoEnd("PROCESS_DATA","Size: " + data.size(), sw.end());
    }
}
