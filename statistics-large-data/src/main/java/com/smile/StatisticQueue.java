package com.smile;

import entity.StatisticsIbft;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatisticQueue {

    List<Map<Integer, StatisticsIbft>> pool = new ArrayList<>();

    private static StatisticQueue instance = new StatisticQueue();

    private StatisticQueue() {}

    public static StatisticQueue getInstance() {
        return instance;
    }

    public synchronized void push(Map<Integer, StatisticsIbft> msg) {
        pool.add(msg);
    }

    public synchronized List<Map<Integer, StatisticsIbft>> pool() throws InterruptedException {
        Lock lock = Lock.getInstance();
        if(!lock.isReadFinish() || !lock.isStatictisFinish()) {
            wait();
        }
        return  this.pool;
    }
}
