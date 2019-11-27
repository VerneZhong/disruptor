package com.zxb.disruptor.ability;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.Executors;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2019-11-26 21:33
 */
public class DisruptorSingleTest {

    public static void main(String[] args) {

        int ringBufferSize = 65536;

        Disruptor<Data> disruptor = new Disruptor<>(() -> new Data(),
                ringBufferSize,
                Executors.defaultThreadFactory(),
                ProducerType.MULTI,
                new YieldingWaitStrategy());

        DataConsumer dataConsumer = new DataConsumer();
        // 消费数据
        disruptor.handleEventsWith(dataConsumer);
        disruptor.start();
        new Thread(() -> {
            RingBuffer<Data> ringBuffer = disruptor.getRingBuffer();
            for (long i = 0; i < Constants.EVENT_NUM_FM; i++) {
                long seq = ringBuffer.next();
                Data data = ringBuffer.get(seq);
                data.setId(i);
                data.setName("c" + i);
                ringBuffer.publish(seq);
            }
            disruptor.shutdown();
        }).start();
    }
}
