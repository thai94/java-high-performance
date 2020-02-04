package com.smile;

import entity.StatisticsIbft;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatisticQueue {

    private static StatisticQueue instance = new StatisticQueue();
    List<Map<Integer, StatisticsIbft>> pool = new ArrayList<>();

    private StatisticQueue() {
    }

    public static StatisticQueue getInstance() {
        return instance;
    }

    public synchronized void push(Map<Integer, StatisticsIbft> msg) {
        pool.add(msg);
    }

    public synchronized List<Map<Integer, StatisticsIbft>> pool() throws InterruptedException {
        WriteLock writeLock = WriteLock.getInstance();
        if (!writeLock.isReadFinish() || !writeLock.isStatictisFinish()) {
            wait();
        }
        return this.pool;
    }
}
