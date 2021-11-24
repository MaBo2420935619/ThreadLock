package exchanger;

import java.util.concurrent.Exchanger;

public class ExchangerTest {
   static Exchanger<String> exchanger=new Exchanger<>();
    static class Show implements Runnable{
        private String name;

        public Show(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
               name= exchanger.exchange(name);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
            System.out.println(name);
        }
    }
    public static void main(String[] args) {
        Exchanger<String> exchanger=new Exchanger<>();
        Thread thread1=new Thread(new Show("1"),"t1");
        Thread thread2=new Thread(new Show("2"),"t2");
        thread1.start();
        thread2.start();
    }
}
