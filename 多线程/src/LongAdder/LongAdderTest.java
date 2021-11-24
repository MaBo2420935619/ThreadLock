package LongAdder;

import java.util.concurrent.atomic.LongAdder;

public class LongAdderTest implements Runnable {
    private final  static int  Max=1000;
    private final   static  int Number=100000;
    LongAdder longAdder = new LongAdder();
    public LongAdder getLongAdder() {
        return longAdder;
    }
    public void setLongAdder(LongAdder longAdder) {
        this.longAdder = longAdder;
    }

    @Override
    public void run() {
        for (int i = 0; i < Number; i++) {
            longAdder.increment();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        LongAdderTest test=new LongAdderTest();
        for (int i = 0; i < Max; i++) {
            Thread thread = new Thread(test::run);
            thread.start();
            thread.join();
        }

        Thread threadX = new Thread(new Runnable() {
            public void run() {
                System.out.println(test.getLongAdder());
                long end = System.currentTimeMillis();
                System.out.println((end-start)+"ms");
            }
        });
        threadX.start();
    }
}
