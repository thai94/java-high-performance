package com.smile;

import java.time.LocalDateTime;

public class Lock {

    public static  int sizeReadThread = 0;
    public static  int sizeStatisticThread = 0;

    int countReadThreadFinsh = 0;
    int countStatisticThreadFinsh = 0;

    private static Lock instance = new Lock();

    public static Lock getInstance() {
        return instance;
    }

    private Lock() {}

    public synchronized void  increaseReadThreadFinsh () {
        this.countReadThreadFinsh++;
    }

    public synchronized void increaseStatisticThreadFinsh() {
        this.countStatisticThreadFinsh++;
    }

    public boolean isReadFinish() {
        return this.countReadThreadFinsh == sizeReadThread;
    }

    public boolean isStatictisFinish() {
        return this.countStatisticThreadFinsh == sizeStatisticThread;
    }
}
