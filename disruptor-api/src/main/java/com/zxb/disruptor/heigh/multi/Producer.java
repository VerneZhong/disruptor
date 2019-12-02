package com.zxb.disruptor.heigh.multi;

import com.lmax.disruptor.RingBuffer;

/**
 * 生产者实例
 *
 * @author Mr.zxb
 * @date 2019-11-29 15:37
 */
public class Producer {

    private RingBuffer<Order> ringBuffer;

    public Producer(RingBuffer<Order> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void sendData(String uuid) {
        long sequence = ringBuffer.next();
        try {
            Order order = ringBuffer.get(sequence);
            order.setId(uuid);
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
