package myLock;

import java.util.LinkedList;
import java.util.List;
/**
 * @Author mabo
 * @Description   实现互斥锁
 */
public class MyLock {
    /**
     * @Author mabo
     * @Description   是否可以加锁
     */

    private volatile int state;

    /**
     * @Author mabo
     * @Description   存储线程集合
     */
    private volatile List threadList=new LinkedList();

    private volatile Long startThreadId=null;

    /**
     * @Author mabo
     * @Description   死锁
     */
    public void lock(){
        Thread thread = Thread.currentThread();
        long id = thread.getId();
        if (startThreadId==null){
            startThreadId=id;
            state++;
        }

    }

}
