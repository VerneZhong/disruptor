package com.zxb.disruptor.quickstart;

import com.lmax.disruptor.EventHandler;

/**
 * 监听事件类，处理数据
 *
 * @author Mr.zxb
 * @date 2019-11-26 22:39
 */
public class OrderEventHandler implements EventHandler<OrderEvent> {

    @Override
    public void onEvent(OrderEvent orderEvent, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("消费者: " + orderEvent.getValue());
    }
}
