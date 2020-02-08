package utils;

public class Log {

    public static void info(String threadName, String content) {
        String log = String.format("[%s][%s] %s", threadName, Thread.currentThread().getName(), content);
        System.out.println(log);
    }

    public static void logStart(String threadName, String content) {
        String log = String.format("Starting [%s][%s] %s", threadName, Thread.currentThread().getName(), content);
        System.out.println(log);
    }

    public static void infoEnd(String threadName, long takeTime) {
        infoEnd(threadName, "", takeTime);
    }

    public static void infoEnd(String threadName, String content, long takeTime) {
        String log = String.format("End [%s][%s] %s. Take time: %s miliseconds", threadName, Thread.currentThread().getName(), content, takeTime);
        System.out.println(log);
    }
}
