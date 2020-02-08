package com.smile.process;

import com.smile.write.WriteLock;
import com.smile.write.DataWriteQueue;
import entity.StatisticsIbft;
import utils.Log;
import utils.StopWatch;

import java.util.List;
import java.util.Map;

public class MergeStatisticThread extends Thread {

    @Override
    public void run() {

        Log.logStart("MERGE_STATISTIC", "");
        StopWatch sw = new StopWatch();
        sw.start();

        StatisticQueue queue = StatisticQueue.getInstance();
        try {
            WriteLock.getInstance().isAbleToStartMergeThead();
            List<Map<Integer, StatisticsIbft>> data = queue.pool();
            if (data.isEmpty()) {
                return;
            }

            Map<Integer, StatisticsIbft> result = data.get(0);
            int size = data.size();
            Map<Integer, StatisticsIbft> processingItem = null;
            for (int i = 1; i < size; i++) {
                processingItem = data.get(i);
                for (Map.Entry<Integer, StatisticsIbft> ibftTran : processingItem.entrySet()) {
                    if (result.containsKey(ibftTran.getKey())) {
                        StatisticsIbft resultIbftTran = result.get(ibftTran.getKey());
                        StatisticsIbft tmpIbftTran = ibftTran.getValue();
                        resultIbftTran.total += tmpIbftTran.total;
                        resultIbftTran.totalAmount += tmpIbftTran.totalAmount;
                        resultIbftTran.totalSucess += tmpIbftTran.totalSucess;
                    } else {
                        result.put(ibftTran.getKey(), ibftTran.getValue());
                    }
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
        Log.infoEnd("MERGE_STATISTIC", sw.end());
    }
}
