import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Database {
    private Map<String, String> data;
    public int maxNumOfReaders;
    private final Lock lock;
    private final Condition readCondition;
    private final Condition writeCondition;
    private int activeReaders;
    private boolean isWriting;


    // can only extend or implement: Thread, Lock + ReentrantLock, Map + HashMap
    // IllegalMonitorStateException, Condition and InterruptedException
    // synchronized, Object (wait, notify, notifyAll)

    // do not use the main classes - worker, reader, writer - made for testing only

    public Database(int maxNumOfReaders) { // do not change parameters
        data = new HashMap<>();
        // Note: You may add fields to the class and initialize them in here. Do not add parameters!
        this.maxNumOfReaders = maxNumOfReaders;
        lock = new ReentrantLock();
        readCondition = lock.newCondition();
        writeCondition = lock.newCondition();
        activeReaders = 0;
        isWriting = false;
    }

    // do not change

    /**
     * used to write to the db
     * @param key key to add
     * @param value the value of the key
     */
    public void put(String key, String value) {
        data.put(key, value);
    }

    /**
     * used to read from the db
     * @param key key from which to get data
     * @return the value of the key
     */
    // do not change
    public String get(String key) {
        return data.get(key);
    }

    // do not change signatures

    /**
     * will be used before reading from the db
     * if a thread calls on this method while another thread writes to the db using put
     * or if k other threads read from the db using get
     * the function will block the thread until it can read
     */
    // avoid Busy Waiting as much as possible
    public void readAcquire() {
        lock.lock();
        try {
            while (isWriting || activeReaders >= maxNumOfReaders) {
                try {
                    readCondition.await();
                } catch (InterruptedException ie) {

                }
            }
            activeReaders++;
        } finally {
            lock.unlock();
        }
    }

    /**
     * can also be used before reading from the db
     * if a thread calls on this method while another thread writes to the db using put
     * or if k other threads read from the db using get
     * will not block the thread
     * @return boolean value according to the test - if thread can start reading or not
     */
    public boolean readTryAcquire() {
        if (isWriting || activeReaders >= maxNumOfReaders) {
            return false;
        } else {
            activeReaders++;
            return true;
        }
    }

    /**
     * will be used after reading from a db
     * signal that a thread finished reading from the db
     */
    // if a thread used this method but doesn't read at this moment from the db
    // throw IllegalMonitorStateException with the message "Illegal read release attempt"
    public void readRelease() {
        lock.lock();
        try {
            if (activeReaders == 0) {
                throw new IllegalMonitorStateException("Illegal read release attempt");
            }
            activeReaders--;
            if (activeReaders == maxNumOfReaders - 1) {
                readCondition.signal();
            }
            if (activeReaders == 0) {
                writeCondition.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * will be used before writing to the db
     * if a thread calls on this method while another thread writes to the db using put
     * or if another thread reads from the db using get
     * the function will block the thread until it can write
     */
    // avoid Busy Waiting as much as possible
    public void writeAcquire() {
       lock.lock();
       try {
           while (isWriting || activeReaders > 0) {
               try {
                   writeCondition.await();
               } catch (InterruptedException ie) {

               }
           }
           isWriting = true;
       } finally {
           lock.unlock();
       }
    }

    /**
     * can also be used before writing to the db
     * if a thread calls on this method while another thread writes to the db using put
     * or if another thread reads from the db using get
     * will not block the thread but
     * @return boolean value according to the test - if thread can start writing or not
     */
    public boolean writeTryAcquire() {
        if (isWriting || activeReaders > 0) {
            return false;
        } else {
            isWriting = true;
            return true;
        }
    }

    /**
     * will be used before writing to the db
     * signal that a thread finished writing to the db
     */
    // if a thread used this method but doesn't write to db at this moment from the db
    // throw IllegalMonitorStateException with the message "Illegal write release attempt"
    public void writeRelease() {
        lock.lock();
        try {
            if (!isWriting) {
                throw new IllegalMonitorStateException("Illegal write release attempt");
            }
            isWriting = false;
            readCondition.signalAll();
            writeCondition.signal();
        } finally {
            lock.unlock();
        }
    }



}