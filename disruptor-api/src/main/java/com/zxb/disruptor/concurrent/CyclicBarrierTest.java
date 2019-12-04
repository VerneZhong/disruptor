package com.zxb.disruptor.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2019-12-04 15:37
 */
public class CyclicBarrierTest {

    public static void main(String[] args) {

        CustomCyclicBarrier cyclicBarrier = new CustomCyclicBarrier(2, () -> System.out.println("merge task."));

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " perform tasks");
            try {
                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " perform tasks");
            try {
                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
