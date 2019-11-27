package com.zxb.disruptor.quickstart;

import com.lmax.disruptor.EventFactory;

/**
 * Event工厂类
 *
 * @author Mr.zxb
 * @date 2019-11-26 22:30
 */
public class OrderEventFactory implements EventFactory<OrderEvent> {

    /**
     * 返回空的数据对象（Event）
     * @return
     */
    @Override
    public OrderEvent newInstance() {
        return new OrderEvent();
    }
}
