package com.zxb.disruptor.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2019-12-04 17:01
 */
public class SemaphoreTest3 {

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Semaphore semaphore = new Semaphore(0);

        executorService.execute(() -> {
            System.out.println(Thread.currentThread().getName() + " A1 task over");
            semaphore.release();
        });
        executorService.execute(() -> {
            System.out.println(Thread.currentThread().getName() + " A2 task over");
            semaphore.release();
        });
        // 等待上2个任务完成
        semaphore.acquire(2);

        executorService.execute(() -> {
            System.out.println(Thread.currentThread().getName() + " B1 task over");
            semaphore.release();
        });
        executorService.execute(() -> {
            System.out.println(Thread.currentThread().getName() + " B1 task over");
            semaphore.release();
        });
        semaphore.acquire(2);

        System.out.println("all task over");
        executorService.shutdown();
    }
}
