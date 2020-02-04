package com.smile;

import java.util.ArrayList;
import java.util.List;

public class ThreadPool {

    List<Thread> pool = new ArrayList<>();

    public synchronized void add(Thread thread) {
        pool.add(thread);
    }

    public void execute() {
        if (pool.size() == 0) {
            return;
        }

        int size = 0;
        Thread thread = null;
        for (int i = 0; i < size; i++) {
            thread = pool.get(i);
            thread.start();
        }
    }
}
