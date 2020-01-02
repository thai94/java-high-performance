package pool;

public class Buffer {
    private byte[] buffer;
    private int size = 0;

    public Buffer(int bufferSize) {
        buffer = new byte[bufferSize];
        size = bufferSize;
    }

    public byte[] getBuffer() {
        return this.buffer;
    }

    public void setSize(int newSize) {
        if(newSize > size) {
            byte[] newBuffer = new byte[newSize];
            System.arraycopy(buffer,0,newBuffer,0,size);
            buffer = newBuffer;
        }
        size = newSize;
    }

    public int getSize() {
        return this.size;
    }
}
