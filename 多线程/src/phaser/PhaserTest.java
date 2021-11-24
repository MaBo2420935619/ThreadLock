package phaser;

import java.util.concurrent.Phaser;

public class PhaserTest {
    public static void main(String[] args) {
        WorkPhaser workPhaser=new WorkPhaser();
        workPhaser.bulkRegister(3);
        for (int i = 1; i < 4; i++) {
            Person person = new Person(workPhaser, String.valueOf(i));
            Thread thread=new Thread(person,String.valueOf(i));
            thread.start();
        }
    }
    private static class Person implements Runnable{
        private Phaser phaser;
        private String name;

        public Person(Phaser phaser, String name) {
            this.phaser = phaser;
            this.name = name;
        }

        @Override
        public void run() {
            work1();
            work2();
            work3();
        }
        private void work1(){
            String playerName = Thread.currentThread().getName();
            System.out.println(playerName+"     吃");
            phaser.arriveAndAwaitAdvance();
        }
        private void work2(){
            String playerName = Thread.currentThread().getName();
            if (playerName.equals("1")||playerName.equals("2")){
                System.out.println(playerName+"     上厕所");
                phaser.arriveAndAwaitAdvance();
            }
            else {
                System.out.println(playerName+"     不上厕所");
                phaser.arriveAndDeregister();
            }
        }
        private void work3() {
            String playerName = Thread.currentThread().getName();
            if (playerName.equals("2")||playerName.equals("3")){
                System.out.println(playerName+"     回家");
                phaser.arriveAndAwaitAdvance();
            }
            else {
                System.out.println(playerName+"     不回家");
                phaser.arriveAndDeregister();
            }
        }
    }
    private static class WorkPhaser extends Phaser {
        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            switch (phase) {
                case 0:
                    System.out.println("所有人都到吃"+registeredParties);
                    return false;
                case 1:
                    System.out.println("1和2一起上厕所"+registeredParties);
                    return false;
                case 2:
                    System.out.println("2和3一起回家"+registeredParties);
                    return true;
                default:
                    return true;
            }
        }

    }

}
