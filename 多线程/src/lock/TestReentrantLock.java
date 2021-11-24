package lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestReentrantLock implements Runnable {
    private   Lock lock = new ReentrantLock();

    int a=0;

    public int getA() {
        return a;
    }

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        TestReentrantLock test = new TestReentrantLock();
        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread(test);
            thread.start();
            thread.join();
        }

        Thread threadX = new Thread(() -> {
            System.out.println(test.getA());
            long end = System.currentTimeMillis();
            System.out.println((end-start)+"ms");
        });
        threadX.start();

    }


    @Override
    public void run() {

        lock.lock();
        try {

            for (int i = 0; i < 100000; i++) {
                a++;
            }
        }finally {
            lock.unlock();
        }
    }
}
