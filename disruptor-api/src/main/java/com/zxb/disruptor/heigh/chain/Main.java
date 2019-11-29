package com.zxb.disruptor.heigh.chain;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.*;

/**
 * Disruptor高级操作
 *
 * @author Mr.zxb
 * @date 2019-11-28 10:02
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        // 构建一个线程池用于提交任务
        ExecutorService executorService = Executors.newSingleThreadExecutor();

//        ExecutorService es2 = Executors.newFixedThreadPool(5);

        // 2的n次方
        int ringBufferSize = 1024 * 1024;

        // 1.构建Disruptor
        Disruptor<Trade> disruptor = new Disruptor<>(Trade::new,
                ringBufferSize,
                Executors.defaultThreadFactory(),
//                es2,
                ProducerType.MULTI,
                new BusySpinWaitStrategy());

        // 2.把多个消费者设置到Disruptor中的handleEventsWith

        // 2.1 串行操作-链式操作
//        disruptor.handleEventsWith(new Handler01())
//                .handleEventsWith(new Handler02())
//                .handleEventsWith(new Handler03());

        // 2.2 并行操作
//        disruptor.handleEventsWith(new Handler01(), new Handler02(), new Handler03());

        // 2.3 菱形操作：类似于 CyclicBarrier的效果 （一）
//        disruptor.handleEventsWith(new Handler01(), new Handler02())
//                .handleEventsWith(new Handler03());

        // 2.3 菱形操作（二）
//        disruptor.handleEventsWith(new Handler01(), new Handler02()).then(new Handler03());

        // 2.4 六边形操作 H1-H2并行，H4-H5并行，最后汇总到H3
        Handler01 h1 = new Handler01();
        Handler02 h2 = new Handler02();
        Handler03 h3 = new Handler03();
        Handler04 h4 = new Handler04();
        Handler05 h5 = new Handler05();
        disruptor.handleEventsWith(h1, h4);
        disruptor.after(h1).handleEventsWith(h2);
        disruptor.after(h4).handleEventsWith(h5);
        disruptor.after(h2, h5).handleEventsWith(h3);

        // 3.启动disruptor
        disruptor.start();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        long startTime = System.currentTimeMillis();

        executorService.execute(new TradePublisher(countDownLatch, disruptor));

        countDownLatch.await();

        // 关闭线程池和相关资源
        disruptor.shutdown();
        executorService.shutdown();
        System.out.println("总耗时：" + (System.currentTimeMillis() - startTime) + "ms");
    }
}
