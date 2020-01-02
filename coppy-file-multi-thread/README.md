# File coppy with mutiple thread implements

- There are two threads.
+ Reader thread: will read the contents of given file as buffer then push it to queue.
+ Writer thread: will pull buffer from the queue then write to coppy file.