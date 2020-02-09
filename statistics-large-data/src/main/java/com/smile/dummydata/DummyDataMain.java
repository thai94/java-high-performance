package com.smile.dummydata;

public class DummyDataMain {
    private static final int POOL_THREAD_SIZE = 2000;
    private static final int WRITE_NUMBER_DATA_PER_THREAD = 50000;

    public static void main(String args[]) {
        int start = 0;
        int end = 0;
        for (int i = 1; i <= POOL_THREAD_SIZE; i++) {
            start = end + 1;
            end = i * WRITE_NUMBER_DATA_PER_THREAD;
            GenerateIbftTransaction genIbftTransaction = new GenerateIbftTransaction(start, end);
            genIbftTransaction.start();
        }
    }
}
