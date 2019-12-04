package com.zxb.disruptor.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2019-12-04 16:53
 */
public class SemaphoreTest2 {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Semaphore semaphore = new Semaphore(0);

        executorService.execute(() -> {
            System.out.println(Thread.currentThread().getName() + " over");
            semaphore.release();
        });
        executorService.execute(() -> {
            System.out.println(Thread.currentThread().getName() + " over");
            semaphore.release();
        });

        semaphore.acquire(2);
        System.out.println("all child thread over.");
        executorService.shutdown();
    }
}
