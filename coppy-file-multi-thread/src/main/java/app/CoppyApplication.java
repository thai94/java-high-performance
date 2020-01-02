package app;

import coppy.FileCoppyReader;
import coppy.FileCopyWriter;
import pool.Pool;
import queue.BufferQueue;

import java.io.FileNotFoundException;

public class CoppyApplication {
    public static void main(String args[]) throws FileNotFoundException, InterruptedException {
        String srcFile = "path_source_file";
        String destFile = "path_dest_file";

        int buffersPoolSize = 50;
        int bufferSize = 1024*4;
        Pool pool = new Pool(buffersPoolSize, bufferSize);
        BufferQueue bufferQueue = new BufferQueue();
        FileCoppyReader reader = new FileCoppyReader(srcFile, pool, bufferQueue);
        FileCopyWriter writer = new FileCopyWriter(destFile, pool, bufferQueue);

        reader.start();
        writer.start();
    }
}
