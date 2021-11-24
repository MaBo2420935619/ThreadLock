package runnable;

public class RunnableTest implements Runnable{
    private final  static int  Max=10000;
    private final   static  int Number=100000;
    private Object object=new Object();
    private    int a=0;
    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    @Override
    public void run() {
        synchronized (object){
            for (int i = 0; i < Number; i++) {
                a++;
            }
        }
    }

    public static void main(String[] args) {
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
                System.out.println((end-start)+"ms");
            }
        });
        threadX.start();
    }
}
