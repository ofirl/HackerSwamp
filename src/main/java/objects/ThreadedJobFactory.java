package objects;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.*;
import interfaces.*;
import managers.Logger;

public class ThreadedJobFactory{
    // class related
    public int maxThreads;
    public List<Thread> threads = new ArrayList<>();
    public ReentrantLock lock = new ReentrantLock();
    public Condition spaceAvailable = lock.newCondition();



    public ThreadedJobFactory(int max) {
        maxThreads = max;
    }

    public ThreadedJob newThread(ThreadEntryPoint r, Object[] args, String name){
        // wait until space is available
        Logger.log("ThreadedJobFactory.newThread", "waiting for space");
        waitUntilSpaceAvailable();
        Logger.log("ThreadedJobFactory.newThread", "got space");
        // create a thread and add it to the list
        ThreadedJob threadedJob = new ThreadedJob(r, args, name, this::threadEnded);

        threads.add(threadedJob);

        return threadedJob;
    }

    public void threadEnded(Thread that) {
        lock.lock();
        try {
            spaceAvailable.signal();
            threads.remove(that);
        }
        finally {
            lock.unlock();
        }
    }

    public void waitUntilSpaceAvailable() {
        lock.lock();
        try {
            while (threads.size() == maxThreads) {
                try {
                    Logger.log("ThreadedJobFactory.waitUntilSpaceAvailable", "waiting...");
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