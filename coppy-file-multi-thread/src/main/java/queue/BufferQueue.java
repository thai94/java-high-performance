package queue;

import pool.Buffer;

import java.sql.BatchUpdateException;
import java.util.LinkedList;
import java.util.Queue;

public class BufferQueue {
    private Queue<Buffer> buffers = new LinkedList<>();

    public synchronized void enqueueBuffer(Buffer b) {
        buffers.add(b);
        if(buffers.size() == 1) {
            notify();
        }
    }

    public synchronized Buffer dequeueBuffer() throws InterruptedException {
        while (buffers.size() == 0) {
            wait();
        }
        return this.buffers.poll();
    }
}
