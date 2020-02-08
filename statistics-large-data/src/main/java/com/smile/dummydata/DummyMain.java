package com.smile.dummydata;

public class DummyMain {
    private static final int POOL_THREAD_SIZE = 50;
    private static final int WRITE_NUMBER_DATA_PER_THREAD = 20000;

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
