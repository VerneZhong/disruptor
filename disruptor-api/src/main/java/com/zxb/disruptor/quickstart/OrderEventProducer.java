package com.zxb.disruptor.quickstart;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * OrderEvent生产者
 *
 * @author Mr.zxb
 * @date 2019-11-27 10:04
 */
public class OrderEventProducer {

    /**
     * 容器
     */
    private RingBuffer<OrderEvent> ringBuffer;

    public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    /**
     * 向容器添加数据
     *
     * @param byteBuffer
     */
    public void sendData(ByteBuffer byteBuffer) {
        // 1、在生产者发送消息的时候，首先需要从我们的ringBuffer里面获取一个可用的序号
        long sequencer = ringBuffer.next();
        try {
            // 2、根据序号，找到具体的 "OrderEvent" 元素，注意：此时获取的OrderEvent对象是一个空属性对象
            OrderEvent orderEvent = ringBuffer.get(sequencer);
            // 3、进行实际的赋值操作
            orderEvent.setValue(byteBuffer.getLong(0));
        } finally {
            // 4、向容器提交发布操作
            ringBuffer.publish(sequencer);
        }
    }
}
