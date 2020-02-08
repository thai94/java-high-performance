package com.smile.write;

import entity.StatisticsIbft;

import java.util.ArrayList;
import java.util.List;

public class DataWriteQueue {
    private static DataWriteQueue instance = new DataWriteQueue();
    private int size = 0;
    private int count = 0;
    private List<StatisticsIbft> data = new ArrayList<>();

    private DataWriteQueue() {
    }

    public static DataWriteQueue getInstance() {
        return instance;
    }

    public synchronized StatisticsIbft pool() throws InterruptedException {
        if (data.size() == 0) {
            wait();
        }

        int index = data.size() - 1;
        StatisticsIbft result = data.get(index);
        data.remove(index);
        return result;
    }

    public synchronized void push(StatisticsIbft msg) {
        data.add(msg);
        count++;
        if (data.size() == 1) {
            notify();
        }
    }

    public synchronized int getSize() throws InterruptedException {
        if (size == 0) {
            wait();
        }
        return size;
    }

    public synchronized void setSize(int size) {
        if (size == 0) {
            notify();
        }
        this.size = size;
    }

    public int getCount() {
        return count;
    }

    public int getPoolSize() {
        return this.data.size();
    }
}
