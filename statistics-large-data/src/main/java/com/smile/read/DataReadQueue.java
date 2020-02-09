package com.smile.read;

import entity.IbftTransaction;

import java.util.ArrayList;
import java.util.List;

public class DataReadQueue {

    private static final int MAX_SIZE = 100;
    private static DataReadQueue queue = new DataReadQueue();
    private List<List<IbftTransaction>> data = new ArrayList<>();

    private DataReadQueue() {
    }

    public static DataReadQueue getInstance() {
        return queue;
    }

    public synchronized List<IbftTransaction> pool() throws InterruptedException {
        while (data.isEmpty()) {
            wait();
        }
        int index = data.size() - 1;
        List<IbftTransaction> ret = data.get(index);
        data.remove(index);
        return ret;
    }

    public synchronized void push(List<IbftTransaction> data) throws InterruptedException {
        notify();
        this.data.add(data);
    }
}
