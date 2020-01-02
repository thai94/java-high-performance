package coppy;

import pool.Buffer;
import pool.Pool;
import queue.BufferQueue;

import java.io.*;
import java.time.LocalDateTime;

public class FileCoppyReader extends Thread {
    private Pool pool;
    private BufferQueue copyBuffers;
    private String filename;
    BufferedInputStream fr;

    public FileCoppyReader(String filename, Pool pool, BufferQueue copyBuffers) throws FileNotFoundException {
        this.filename = filename;
        this.pool = pool;
        this.copyBuffers = copyBuffers;
        fr = new BufferedInputStream(new FileInputStream(filename));
    }

    public void run() {
        Buffer buffer = null;
        int bytesRead = 0;

        try {
            do {
                try {
                    buffer = pool.use();
                    bytesRead = fr.read(buffer.getBuffer());
                } catch (InterruptedException | IOException e) {
                    buffer.setSize(0);
                    bytesRead = 0;
                    e.printStackTrace();
                }

                if (bytesRead < 0) {
                    buffer.setSize(0);
                } else {
                    buffer.setSize(bytesRead);
                }
                copyBuffers.enqueueBuffer(buffer);
            } while (bytesRead > 0);
        } finally {
            System.out.println("FileCoppyReader finished");
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
