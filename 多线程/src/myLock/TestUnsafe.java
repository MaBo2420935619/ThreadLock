package myLock;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author 陈方岩
 * @date 2020/1/12 22:27
 */
public class TestUnsafe {
    volatile int n = 0;
    private static Unsafe unsafe = null;
    private static long fieldOffset;

    static {
        //unsafe = Unsafe.getUnsafe();
        Field unsafe1 = null;//获取Unsafe的theUnsafe属性
        try {
            unsafe1 = Unsafe.class.getDeclaredField("theUnsafe");
            unsafe1.setAccessible(true);
            unsafe = (Unsafe) unsafe1.get(null);//unsafe实例对象

            Field n1 = TestUnsafe.class.getDeclaredField("n");
            fieldOffset = unsafe.objectFieldOffset(n1);//获取偏移量
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    public void add() {
        int intVolatile = unsafe.getIntVolatile(this, fieldOffset);//获取值
        for (; ; ) {//防止伪唤醒
            unsafe.compareAndSwapInt(this, fieldOffset, intVolatile, intVolatile + 1);// add  +1
            break;
        }
    }

    public static void main(String[] args) {
        TestUnsafe testUnsafe = new TestUnsafe();
        for (int i = 0; i < 6; i++) {//创建6个线程实现
            new Thread(() -> {
                testUnsafe.add();
            }).start();
        }
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(testUnsafe.n);

    }

}
