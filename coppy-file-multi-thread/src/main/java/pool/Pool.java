package pool;

import java.util.ArrayList;
import java.util.List;

public class Pool {
    List<Buffer> freeBufferList = new ArrayList<>();
    int buffers = 10, bufferSize = 1024;

    public Pool(int buffers, int bufferSize) {
        this.buffers = buffers;
        this.bufferSize = bufferSize;
        for(int i = 0; i < buffers; i++) {
            freeBufferList.add(new Buffer(bufferSize));
        }
    }

    public synchronized Buffer use() throws InterruptedException {
        while (freeBufferList.size() == 0) {
            wait();
        }
        int lastIndex = freeBufferList.size() - 1;
        Buffer nextBuffer = (Buffer) freeBufferList.get(lastIndex);
        freeBufferList.remove(lastIndex);
        return nextBuffer;
    }

    public synchronized void release(Buffer oldBuffer) {
        if (freeBufferList.contains(oldBuffer)) {
            return;
        }
        if (oldBuffer.getSize() < bufferSize) {
            oldBuffer.setSize(bufferSize);
        }
        freeBufferList.add(oldBuffer);

        if (freeBufferList.size() == 1) {
            notify();
        }
    }

}
