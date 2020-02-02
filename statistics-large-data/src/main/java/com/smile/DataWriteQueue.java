package com.smile;

import entity.StatisticsIbft;

import java.util.ArrayList;
import java.util.List;

public class DataWriteQueue {

    List<StatisticsIbft> data = new ArrayList<>();

    private static DataWriteQueue instance = new DataWriteQueue();

    private DataWriteQueue() {}

    public static DataWriteQueue getInstance() {
        return instance;
    }

    public synchronized StatisticsIbft pool() throws InterruptedException {
        if(data.size() == 0) {
            wait();
        }

        StatisticsIbft result = data.get(data.size() - 1);
        data.remove(data.size() - 1);
        return result;
    }

    public synchronized void push(StatisticsIbft msg) {
        data.add(msg);
        if (data.size() == 1) {
            notify();
        }
    }

}
