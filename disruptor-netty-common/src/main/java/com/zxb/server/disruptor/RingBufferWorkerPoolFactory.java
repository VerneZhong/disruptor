package com.zxb.server.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.WorkerPool;
import com.lmax.disruptor.dsl.ProducerType;
import com.zxb.server.entity.TranslatorDataWrapper;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Disruptor 组件
 *
 * @author Mr.zxb
 * @date 2019-12-08 15:55
 */
public class RingBufferWorkerPoolFactory {

    private RingBufferWorkerPoolFactory() {

    }

    private static class SingletonHolder {
        public static final RingBufferWorkerPoolFactory INSTANCE = new RingBufferWorkerPoolFactory();
    }

    public static RingBufferWorkerPoolFactory newInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static final int CPUS = Runtime.getRuntime().availableProcessors();

    public static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(CPUS,
            CPUS,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),
            (ThreadFactory) Thread::new);

    private static Map<String, MessageProducer> producerMap = new ConcurrentHashMap<>();

    private static Map<String, MessageConsumer> consumerMap = new ConcurrentHashMap<>();

    private RingBuffer<TranslatorDataWrapper> ringBuffer;

    private WorkerPool<TranslatorDataWrapper> workerPool;

    private SequenceBarrier sequenceBarrier;

    public void initAndStart(ProducerType producerType, int bufferSize, WaitStrategy waitStrategy, MessageConsumer[] messageConsumers) {
        // 1. 构建RingBuffer对象
        this.ringBuffer = RingBuffer.create(producerType,
                TranslatorDataWrapper::new,
                bufferSize,
                waitStrategy);

        // 2. 设置序号栅栏
        this.sequenceBarrier = ringBuffer.newBarrier();

        // 3. 设置工作池
        this.workerPool = new WorkerPool<>(ringBuffer, sequenceBarrier, new EventExceptionHandler(), messageConsumers);

        // 4. 把所构建的消费者置入池中
        for (MessageConsumer consumer : messageConsumers) {
            consumerMap.put(consumer.getConsumerId(), consumer);
        }

        // 5. 添加sequences
        this.ringBuffer.addGatingSequences(workerPool.getWorkerSequences());

        // 6. 启动工作池

        this.workerPool.start(EXECUTOR);
    }

    /**
     * 生产者容器里获取生产者
     * @param producerId
     * @return
     */
    public MessageProducer getMessageProducer(String producerId) {
//        MessageProducer messageProducer = producerMap.get(producerId);
//        if (messageProducer == null) {
//            messageProducer = new MessageProducer(ringBuffer);
//            producerMap.put(producerId, messageProducer);
//        }
        return producerMap.computeIfAbsent(producerId, id -> new MessageProducer(id, ringBuffer));
    }

}
