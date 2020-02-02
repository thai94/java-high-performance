package com.smile;

import entity.IbftTransaction;

import java.util.ArrayList;
import java.util.List;

public class DataReadQueue {

    private static final int MAX_SIZE = 100;

    private List<List<IbftTransaction>> data = new ArrayList<>();

    private DataReadQueue() {}

    private static DataReadQueue queue = new DataReadQueue();

    public synchronized static DataReadQueue getInstance() {
        return queue;
    }

    public synchronized List<IbftTransaction> pool() throws InterruptedException {
        if(data.isEmpty()) {
            wait();
        }
        if(data.size() == MAX_SIZE) {
            notify();
        }
        List<IbftTransaction> ret = data.get(data.size() - 1);
        data.remove(data.size() - 1);
        return ret;
    }

    public synchronized void push(List<IbftTransaction> data) throws InterruptedException {
        while(data.size() >= MAX_SIZE) {
            wait();
        }
        if(this.data.isEmpty()) {
            notify();
        }
        this.data.add(data);
    }
}
