package cyclicBarrier;

import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest {
    // 自定义工作线程
    private static class Worker implements Runnable {
        private static CyclicBarrier cyclicBarrier=new CyclicBarrier(5);

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + "开始等待其他线程");
                cyclicBarrier.await();
                System.out.println(Thread.currentThread().getName() + "执行");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        for (int i = 0; i < 100; i++) {
            System.out.println("创建工作线程" + i);
            Thread thread=new Thread(new Worker());
            thread.start();

        }
    }
}
