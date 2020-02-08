package utils;

public class StopWatch {
    public long start = 0;
    public long end = 0;

    public void start() {
        start = System.currentTimeMillis();
    }

    public long end() {
        end = System.currentTimeMillis();
        return end - start;
    }


}
