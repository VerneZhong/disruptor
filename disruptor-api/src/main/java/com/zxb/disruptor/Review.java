package com.zxb.disruptor;

import com.zxb.disruptor.common.SleepUtil;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2019-12-02 09:43
 */
public class Review {

    public static void main(String[] args) {

//        Map<String, String> map = new ConcurrentHashMap<>();

//        CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();

//        copyOnWriteArrayList.add("a");

        Object lock = new Object();
        Thread thread = new Thread(() -> {
            int sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += i;
            }
            // wait和notify，等待和唤醒线程
//            synchronized (lock) {
//                try {
//                    lock.wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }

            SleepUtil.sleep(4, TimeUnit.SECONDS);
            // LockSupport挂起和唤醒线程
            LockSupport.park();

            System.out.println("sum = " + sum);
        });

        thread.start();

        SleepUtil.sleep(1, TimeUnit.SECONDS);

//        synchronized (lock) {
//            lock.notify();
//        }

        LockSupport.unpark(thread);

        // 线程池创建案例
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,
                Runtime.getRuntime().availableProcessors() * 2,
                60L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(200),
                r -> {
                    Thread t = new Thread(r);
                    t.setName("order-thread");
                    if (t.isDaemon()) {
                        t.setDaemon(false);
                    }
                    if (Thread.NORM_PRIORITY != t.getPriority()) {
                        t.setPriority(Thread.NORM_PRIORITY);
                    }
                    return t;
                },
                (r, executor) -> System.out.println("拒绝策略：" + r));

        threadPoolExecutor.execute(() -> {});
        threadPoolExecutor.submit(() -> {});
        threadPoolExecutor.shutdown();
    }
}
