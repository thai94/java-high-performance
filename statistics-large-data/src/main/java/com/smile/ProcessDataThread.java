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
            if(data == null || data.size() == 0) {
                return;
            }

            Map<Integer, StatisticsIbft> statisticsIbft = new HashMap<>();
            int size = data.size();
            IbftTransaction tran = null;
            StatisticsIbft ibftSts = null;
            for (int i = 0; i< size; i++) {
                tran = data.get(i);
                if(statisticsIbft.containsKey(tran.userId)) {
                    ibftSts = statisticsIbft.get(tran.userId);
                }
                ibftSts = new StatisticsIbft();
                ibftSts.total += 1;
                ibftSts.totalSucess += tran.status;
                ibftSts.totalAmount += tran.amount;
            }

            StatisticQueue queue = StatisticQueue.getInstance();
            queue.push(statisticsIbft);
            Lock lock = Lock.getInstance();
            lock.increaseStatisticThreadFinsh();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
