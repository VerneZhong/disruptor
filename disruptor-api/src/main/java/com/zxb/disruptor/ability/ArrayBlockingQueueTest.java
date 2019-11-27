package com.zxb.disruptor.ability;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2019-11-26 17:33
 */
public class ArrayBlockingQueueTest {

    public static void main(String[] args) {

        BlockingQueue arrayBlockingQueue = new ArrayBlockingQueue<Data>(Constants.EVENT_NUM_FM);
//        BlockingQueue arrayBlockingQueue = new LinkedBlockingQueue<Data>(Constants.EVENT_NUM_FM);
        long startTime = System.currentTimeMillis();

        new Thread(() -> {
            long i = 0;
            while (i < Constants.EVENT_NUM_FM) {
                Data data = new Data(i, "c" + i);
                try {
                    // 存入数据
                    arrayBlockingQueue.put(data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }
        }).start();

        new Thread(() -> {
            int k = 0;
            while (k < Constants.EVENT_NUM_FM) {
                try {
                    // 获取数据
                    arrayBlockingQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                k++;
            }
            long endTime = System.currentTimeMillis();
            System.out.println("ArrayBlockingQueue costTime = " + (endTime - startTime) + "ms");
        }).start();
    }
}
