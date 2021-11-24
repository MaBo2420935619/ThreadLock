package atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerTest implements Runnable {
    private final  static int  Max=10000;
    private final   static  int Number=100000;
    AtomicInteger atomicInteger=new AtomicInteger(0);

    public AtomicInteger getAtomicInteger() {
        return atomicInteger;
    }

    public void setAtomicInteger(AtomicInteger atomicInteger) {
        this.atomicInteger = atomicInteger;
    }

    @Override
    public void run() {
        for (int i = 0; i < Number; i++) {
            atomicInteger.incrementAndGet();
        }
    }
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        AtomicIntegerTest test=new AtomicIntegerTest();
        for (int i = 0; i < Max; i++) {
            Thread thread = new Thread(test::run);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Thread threadX = new Thread(new Runnable() {
            public void run() {
                System.out.println(test.getAtomicInteger());
                long end = System.currentTimeMillis();
                System.out.println((end-start)+"ms");
            }
        });
        threadX.start();
    }
}
