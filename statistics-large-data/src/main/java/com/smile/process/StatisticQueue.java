package com.smile.process;

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
        if (pool.isEmpty()) {
            notify();
        }
        pool.add(msg);
    }

    public synchronized List<Map<Integer, StatisticsIbft>> pool() throws InterruptedException {
        if (pool.isEmpty()) {
            wait();
        }
        return this.pool;
    }
}
