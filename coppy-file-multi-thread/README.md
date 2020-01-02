# File coppy with mutiple thread implements

## Overview

There are two threads.

- Reader thread: will read the contents of given file as buffer then push it to queue.
- Writer thread: will pull buffer from the queue then write to coppy file.

## Details of the various classes

- Buffer: This class is an example of a wrapper class. Wrapper classes are often used in Java to make it easier to put data that belong together in a container. A Buffer is used to store a block of data that has been read from the source file and is to be written to the destination file.

- Pool: This class shows a useful pattern for minimizing the overhead associated with “garbage collection.” A Pool is simply a fixed collection of Buffer instances. These instances are borrowed from the Pool and returned as needed. This class actually makes use of conditions.

- BufferQueue: This class is used to maintain, in first-in-first-out (FIFO) order, a list of Buffer instances waiting to be written to the destination file. Unlike the Pool, any number of instances can appear on the BufferQueue. Conditions are also used by BufferQueue to provide flow control between the reader and the writer.

- FileCopyReader: An instance of this class (which is a subclass of Thread) will read blocks from a file one at a time and write them to a BufferQueue that is shared with an instance of FileCopyWriter. A Pool is also shared between an instance of this class and an instance of FileCopyWriter.

- FileCopyWriter: An instance of this class (which is a subclass of Thread) will read blocks from a BufferQueue instance and write them to the destination file. It is this class that is actually responsible for doing the final copying.
