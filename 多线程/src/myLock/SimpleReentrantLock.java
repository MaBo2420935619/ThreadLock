package myLock;
import java.lang.reflect.Field;
import java.util.concurrent.locks.LockSupport;
import sun.misc.Unsafe;





/**
 * @Author mabo
 * @Description   可重入锁CAS
 */
public class SimpleReentrantLock {
    private volatile static Unsafe unsafe;
    /** state内存地址给cas操作用的*/
    private volatile static long stateoffset;

    private volatile static long headoffset;

    private volatile static long tailoffset;

    private volatile static long waitoffset;

    /** 锁的状态，不为0表示有线程占有锁*/
    public volatile int state;

    public volatile Node head = new Node();

    public volatile Node tail = head;
    SimpleReentrantLock(){
    }
    public static void main(String[] args) throws InterruptedException {

        final SimpleReentrantLock  lock = new SimpleReentrantLock();

        for (int i = 0; i < 10; i++) {
            Thread thread1 = new Thread(() -> {
                lock.lock();
                for (int j = 0; j < 5; j++) {
                    System.out.println(Thread.currentThread().getName());
                }
                lock.unlock();

            },String.valueOf(i));
            thread1 .start();
        }
    }




    /**

     * 获取锁，可重入

     * @return true：获取锁成功   false：获取锁失败

     */

    public void lock(){
        acquire(1);
    }


    /**
     * 释放锁，可重入
     * @return true：获取锁成功   false：获取锁失败
     */
    public boolean unlock(){

        for(;;){

            if(setCASWait(head,2, 0)){

                if(state-1 == 0){

                    Node next = head.next;

                    if(next != null){

                        if(next.next != null){

                            head.next = next.next;

                        }

                        unsafe.unpark(next.thread);

                    }

                }

                state=state-1;

                setCASWait(head,0, 2);

                return true;

            }

        }

    }



    /**

     * 获取锁

     * @param i

     * @return

     */

    public void acquire(int i){

        for(;;){

            try{

                if(tryAcquire(1)){



                    return;

                }

                Node node = new Node();

                node.thread = Thread.currentThread();



                if(addQueue(node)){

                    setCASWait(head,1, 2);

                    return;

                }

                LockSupport.park();

            }finally{

            }
        }

    }





    /**
     * 尝试获取锁
     *@return 获取锁是否成功
     */
    public boolean tryAcquire(int i){

        if(setCASState(0,i)){

            return true;

        }

        return false;

    }



    /**

     * 添加等待队列

     * @param node

     * @return

     */

    public boolean addQueue(Node node){

        for(;;){

            if(setCASWait(head,2, 1)){

                if(tryAcquire(1)){

                    return true;

                }

                Node oldtail = tail;

                if(setCASTail(oldtail, node)){

                    oldtail.next = node;        // 竞技条件

                    setCASWait(head,1, 2);

                    return false;

                }

            }

        }

    }



    /**

     * 操作堆外内存

     * @return

     */

    public static Unsafe getUnsafe(){

        Field f1;

        try {

            f1 = Unsafe.class.getDeclaredField("theUnsafe");

            f1.setAccessible(true);

            return (Unsafe) f1.get(null);

        } catch (NoSuchFieldException e) {

            e.printStackTrace();

        } catch (SecurityException e) {

            e.printStackTrace();

        } catch (IllegalArgumentException e) {

            e.printStackTrace();

        } catch (IllegalAccessException e) {

            e.printStackTrace();

        }



        return null;

    }



    /**

     * cas赋值state

     * @param exche     判断值

     * @param update    更新值

     * @return true：表示修改成功，false修改失败

     */

    public boolean setCASState(int exche,int update){

        return unsafe.compareAndSwapInt(this, stateoffset, exche, update);

    }



    /**

     * cas赋值head

     * @param exche     判断值

     * @param update    更新值

     * @return true：表示修改成功，false修改失败

     */

    public boolean setCASHead(Node exche,Node update){

        return unsafe.compareAndSwapObject(this, headoffset, exche, update);

    }



    /**

     * cas赋值tail

     * @param exche     判断值

     * @param update    更新值

     * @return true：表示修改成功，false修改失败

     */

    public boolean setCASTail(Node exche,Node update){

        return unsafe.compareAndSwapObject(this, tailoffset, exche, update);

    }



    /**

     * cas赋值tail

     * @param exche     判断值

     * @param update    更新值

     * @return true：表示修改成功，false修改失败

     */

    public boolean setCASWait(Node obj,int exche,int update){

        return unsafe.compareAndSwapInt(obj, waitoffset, exche, update);

    }



    /** 给内存地址赋值 */

    static{

        unsafe = getUnsafe();

        try {

            stateoffset = unsafe.objectFieldOffset(SimpleReentrantLock.class.getField("state"));

            headoffset = unsafe.objectFieldOffset(SimpleReentrantLock.class.getField("head"));

            tailoffset = unsafe.objectFieldOffset(SimpleReentrantLock.class.getField("tail"));

            waitoffset = unsafe.objectFieldOffset(Node.class.getField("wait"));

        } catch (NoSuchFieldException e) {

            e.printStackTrace();

        } catch (SecurityException e) {

            e.printStackTrace();

        }

    }



    static class Node{

        /**1表示可释放锁 2都可以  3表示可获取锁 */

        public volatile int wait = 2;

        volatile Node next;

        volatile Thread thread;

    }
}
