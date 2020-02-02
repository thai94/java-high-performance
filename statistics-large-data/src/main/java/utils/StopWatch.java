package utils;

public class StopWatch {
    public long start = 0;
    public long end = 0;

    public void start() {
        start = System.currentTimeMillis();
    }

    public void end() {
        end = System.currentTimeMillis();
        System.out.println(String.format("The %s take time: %s", Thread.currentThread().getName(), (end - start)));
    }


}
