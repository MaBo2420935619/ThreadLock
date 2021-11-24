package lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestTryLock {
    private static int a=0;

    public static int getA() {
        return a;
    }

    public static void setA(int a) {
        TestTryLock.a = a;
    }

    private static Lock lock=new ReentrantLock();;
    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        TestTryLock test=new TestTryLock();

        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(test::add);
            thread.start();
            thread.join();
        }
        Thread threadX = new Thread(new Runnable() {
            public void run() {
                System.out.println(test.getA());
                long end = System.currentTimeMillis();
                System.out.println((end-start)+"ms");
            }
        });
        threadX.start();

    }
    public void add(){
        try {
            lock.tryLock(1, TimeUnit.SECONDS);
            a++;
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }
}
