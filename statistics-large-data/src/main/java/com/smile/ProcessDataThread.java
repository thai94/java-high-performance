package com.smile;

import entity.IbftTransaction;
import entity.StatisticsIbft;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessDataThread extends Thread {

    @Override
    public void run() {
        try {
            List<IbftTransaction> data = DataReadQueue.getInstance().pool();
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
                }
                tmpIbftSts = new StatisticsIbft();
                tmpIbftSts.total += 1;
                tmpIbftSts.totalSucess += processingTran.status;
                tmpIbftSts.totalAmount += processingTran.amount;
            }

            StatisticQueue queue = StatisticQueue.getInstance();
            queue.push(statisticsIbft);
            WriteLock writeLock = WriteLock.getInstance();
            writeLock.increaseStatisticThreadFinsh();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
