package com.zxb.disruptor.heigh.multi;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WorkerPool;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import com.zxb.disruptor.common.SleepUtil;

import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Disruptor 多生产者和多消费者模式
 *
 * @author Mr.zxb
 * @date 2019-11-29 14:05
 */
public class MultiMain {

    public static void main(String[] args) {
        // 线程池
        int cpus = 5;
        ExecutorService executor = new ThreadPoolExecutor(cpus, cpus,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                (ThreadFactory) Thread::new);

        ExecutorService executor2 = Executors.newFixedThreadPool(10);

        // 1.创建RingBuffer
        RingBuffer<Order> ringBuffer = RingBuffer.create(ProducerType.MULTI,
                Order::new,
                1024 * 1024,
                new YieldingWaitStrategy());

        // 2.通过ringBuffer创建一个屏障
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        // 3.构建多消费者
        Consumer[] consumers = IntStream.range(0, 10)
                .boxed()
                .map(i -> new Consumer("C" + i))
                .collect(Collectors.toList()).toArray(new Consumer[10]);

        // 4.构建多消费者工作池
        WorkerPool<Order> orderWorkerPool = new WorkerPool<>(ringBuffer,
                sequenceBarrier,
                new EventExceptionHandler(),
                consumers);

        // 5. 设置多个消费者多sequence序号，用于单独统计消费进度，并且设置到ringBuffer中
        ringBuffer.addGatingSequences(orderWorkerPool.getWorkerSequences());

        // 6.启动workerPool
        orderWorkerPool.start(executor);

        CountDownLatch countDownLatch = new CountDownLatch(1);

        // 创建100个线程
        int loop = 100;
        for (int i = 0; i < loop; i++) {
            Producer producer = new Producer(ringBuffer);
            executor2.execute(() -> {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int i1 = 0; i1 < loop; i1++) {
                    producer.sendData(UUID.randomUUID().toString());
                }
            });
        }

        System.out.println("线程创建完毕，开始生产数据...");
        countDownLatch.countDown();

        SleepUtil.sleep(5, TimeUnit.SECONDS);
        System.out.println("任务的总数：" + consumers[2].getCount());

        executor.shutdown();
        executor2.shutdown();
    }
}
