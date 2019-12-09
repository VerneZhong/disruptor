package com.zxb.server.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.zxb.server.entity.TranslatorData;
import com.zxb.server.entity.TranslatorDataWrapper;
import io.netty.channel.ChannelHandlerContext;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2019-12-08 15:58
 */
public class MessageProducer {

    private String producerId;

    private RingBuffer<TranslatorDataWrapper> ringBuffer;

    public MessageProducer(String producerId, RingBuffer<TranslatorDataWrapper> ringBuffer) {
        this.producerId = producerId;
        this.ringBuffer = ringBuffer;
    }

    public void onData(TranslatorData data, ChannelHandlerContext context) {
        long sequence = ringBuffer.next();
        try {
            TranslatorDataWrapper wrapper = ringBuffer.get(sequence);
            wrapper.setData(data);
            wrapper.setHandlerContext(context);
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
