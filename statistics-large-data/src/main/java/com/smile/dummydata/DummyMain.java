package com.smile.dummydata;

public class DummyMain {
    private static final int POOL_THREAD_SIZE = 50;

    public static void main(String args[]) {
        int start = 0;
        int end = 0;
        for (int i = 1; i <= POOL_THREAD_SIZE; i++) {
            start = end + 1;
            end = i * 1000000;
            GenIbftTransaction genIbftTransaction = new GenIbftTransaction(start, end);
            genIbftTransaction.start();
        }
    }
}
