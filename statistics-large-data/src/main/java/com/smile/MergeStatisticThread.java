package com.smile;

import entity.StatisticsIbft;

import java.util.List;
import java.util.Map;

public class MergeStatisticThread extends Thread {

    @Override
    public void run() {
        StatisticQueue queue = StatisticQueue.getInstance();
        try {
            List<Map<Integer, StatisticsIbft>> data = queue.pool();
            if (data.isEmpty()) {
                return;
            }

            Map<Integer, StatisticsIbft> result = data.get(0);
            Map<Integer, StatisticsIbft> processingItem = null;
            int size = data.size();
            for (int i = 1; i < size; i++) {
                processingItem = data.get(i);
                for (Map.Entry<Integer, StatisticsIbft> ibftTran : processingItem.entrySet()) {
                    if (result.containsKey(ibftTran.getKey())) {
                        StatisticsIbft resultIbftTran = result.get(ibftTran.getKey());
                        StatisticsIbft tmpIbftTran = ibftTran.getValue();
                        resultIbftTran.total += tmpIbftTran.total;
                        resultIbftTran.totalAmount += tmpIbftTran.totalAmount;
                        resultIbftTran.totalSucess += tmpIbftTran.totalSucess;
                        continue;
                    }
                    result.put(ibftTran.getKey(), ibftTran.getValue());
                }
            }

            DataWriteQueue writeQueue = DataWriteQueue.getInstance();
            writeQueue.setSize(result.size());

            for (Map.Entry<Integer, StatisticsIbft> ibftTran : result.entrySet()) {
                writeQueue.push(ibftTran.getValue());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
