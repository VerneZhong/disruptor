package com.zxb.disruptor.concurrent;

import com.zxb.disruptor.common.UnsafeUtil;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Unsafe class Test
 *
 * @author Mr.zxb
 * @date 2019-12-03 10:38
 */
public class UnsafeTest {

    private int state;

    public static void main(String[] args) throws Exception {
        // 初始化待修改实例
        UnsafeTest unsafeTest = new UnsafeTest();
        Unsafe unsafe = UnsafeUtil.reflectGetUnsafe();

        long state = unsafe.objectFieldOffset(UnsafeTest.class.getDeclaredField("state"));
        // cas更新
        boolean success = unsafe.compareAndSwapInt(unsafeTest, state, 0, 1);
        System.out.println("success = " + success);
        int andAddInt = unsafe.getAndAddInt(unsafeTest, state, 2);
        System.out.println("old value: " + andAddInt);

        // park 阻塞当前线程
//        unsafe.park(false, 0L);

        // 内存读屏障
//        unsafe.loadFence();
        // 内存全屏障
        unsafe.fullFence();
        // 内存写屏障
//        unsafe.storeFence();

        System.out.println("new state: " + unsafeTest.state);
    }

}
