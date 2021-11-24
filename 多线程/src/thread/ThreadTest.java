package thread;

import LongAdder.LongAdderTest;
import atomic.AtomicIntegerTest;
import runnable.RunnableTest;

public class ThreadTest extends Thread{
    private final  static int  Max=10000;
    @Override
    public void run(){
        System.out.println("asda");
    }
//    public static void main(String[] args) {
//        Thread th=new ThreadTest();
//        th.start();
//    }

    public static void main(String[] args) throws InterruptedException {
        /**
         * @Author mabo
         * @Description   AtomicInteger
         */

        long start1 = System.currentTimeMillis();
        AtomicIntegerTest test1=new AtomicIntegerTest();
        for (int i = 0; i < Max; i++) {
            Thread thread = new Thread(test1::run);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Thread threadX1 = new Thread(new Runnable() {
            public void run() {
                System.out.println(test1.getAtomicInteger());
                long end = System.currentTimeMillis();
                System.out.println("Atomic"+(end-start1)+"ms");
            }
        });
        threadX1.start();



        /**
         * @Author mabo
         * @Description LongAdder
         */
        long start2 = System.currentTimeMillis();
        LongAdderTest test2=new LongAdderTest();
        for (int i = 0; i < Max; i++) {
            Thread thread = new Thread(test2::run);
            thread.start();
            thread.join();
        }

        Thread threadX2 = new Thread(new Runnable() {
            public void run() {
                System.out.println(test2.getLongAdder());
                long end = System.currentTimeMillis();
                System.out.println("LongAdder"+(end-start2)+"ms");
            }
        });
        threadX2.start();



        /**
         * @Author mabo
         * @Description Runnable
         */

        long start = System.currentTimeMillis();
        RunnableTest test=new RunnableTest();
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
                System.out.println(test.getA());
                long end = System.currentTimeMillis();
                System.out.println("syn"+(end-start)+"ms");
            }
        });
        threadX.start();
    }
}
