package readAndWriteLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadAndWriteLockTest {
   static   ReadWriteLock readWriteLock=new ReentrantReadWriteLock();
    static   Lock read=readWriteLock.readLock();
    static Lock write=readWriteLock.writeLock();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    read.lock();
                    Thread.sleep(1000);
                    System.out.println("我在读");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    read.unlock();
                }
            }).start();
        }
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                try {
                    write.lock();
                    Thread.sleep(1000);
                    System.out.println("我在写");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    write.unlock();
                }
            }).start();
        }
    }
}
