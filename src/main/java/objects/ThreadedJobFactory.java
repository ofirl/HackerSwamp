package objects;

import java.util.List;
import java.util.concurrent.locks.*;
import interfaces.*;

public class ThreadedJobFactory{
    // class related
    public int maxThreads;
    public List<Thread> threads;
    public ReentrantLock lock = new ReentrantLock();
    public Condition spaceAvailable = lock.newCondition();



    public ThreadedJobFactory(int max) {
        maxThreads = max;
    }

    public ThreadedJob newThread(ThreadEntryPoint r, Object[] args, String name){
        // wait until space is available
        waitUntilSpaceAvailable();
        // create a thread and add it to the list
        ThreadedJob threadedJob = new ThreadedJob(r, args, name, this::threadEnded);

        threads.add(threadedJob);

        return threadedJob;
    }

    public void threadEnded() {
        lock.lock();
        try {
            spaceAvailable.signal();
        }
        finally {
            lock.unlock();
        }
    }

    public void waitUntilSpaceAvailable() {
        lock.lock();
        try {
            if (threads.size() == maxThreads) {
                try {
                    spaceAvailable.await();
                }
                catch (Exception e) {
                }
            }
        }
        finally {
            lock.unlock();
        }
    }
}