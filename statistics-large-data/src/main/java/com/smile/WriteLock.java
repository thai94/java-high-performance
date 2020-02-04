package com.smile;

/**
 * All write data thread only run when all read data finished and all statictis data thread finished.
 */
public class WriteLock {

    public static int sizeReadThread = 0;
    public static int sizeStatisticThread = 0;
    private static WriteLock instance = new WriteLock();
    int countReadThreadFinsh = 0;
    int countStatisticThreadFinsh = 0;

    private WriteLock() {
    }

    public static WriteLock getInstance() {
        return instance;
    }

    public synchronized void increaseReadThreadFinsh() {
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
