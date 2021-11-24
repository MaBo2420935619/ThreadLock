package countdownLatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CountdownLatchTest {
    static int a=0;
    public static void main(String[] args) throws InterruptedException {

        Lock lock=new ReentrantLock(true);
        Thread threads[]=new Thread[100];
        CountDownLatch countDownLatch=new CountDownLatch(threads.length);

        for (Thread t : threads) {
            t=new Thread(() -> {
                try {
                    lock.lock();
                    a++;
                    countDownLatch.countDown();
                } finally {
                    lock.unlock();
                }
            });
            t.start();
        }
        countDownLatch.await();
        System.out.println(a);
    }
}
