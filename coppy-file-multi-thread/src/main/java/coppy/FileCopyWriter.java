package coppy;

import pool.Buffer;
import pool.Pool;
import queue.BufferQueue;

import java.io.*;

public class FileCopyWriter extends Thread {
    private Pool pool;
    private BufferQueue copyBuffers;
    private String filename;
    BufferedOutputStream fw;

    public FileCopyWriter(String filename, Pool pool, BufferQueue copyBuffers) throws FileNotFoundException {
        this.filename = filename;
        this.pool = pool;
        this.copyBuffers = copyBuffers;
        fw = new BufferedOutputStream(new FileOutputStream(filename));
    }

    @Override
    public void run() {
        Buffer buffer;

        try {
            while(true) {
                try {
                    buffer = copyBuffers.dequeueBuffer();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }

                if(buffer.getSize() > 0) {
                    try {
                        fw.write(buffer.getBuffer(), 0, buffer.getSize());
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    } finally {
                        pool.release(buffer);
                    }
                } else {
                    break;
                }
            }
        } finally {
            System.out.println("FileCopyWriter finished");
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
