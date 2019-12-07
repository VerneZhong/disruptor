package com.zxb.disruptor.quickstart;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import com.zxb.disruptor.common.SleepUtil;

import java.util.concurrent.TimeUnit;

/**
 * 监听事件类，处理数据
 *
 * @author Mr.zxb
 * @date 2019-11-26 22:39
 */
public class OrderEventHandler implements EventHandler<OrderEvent>, WorkHandler<OrderEvent> {

    @Override
    public void onEvent(OrderEvent orderEvent, long sequence, boolean endOfBatch) throws Exception {
//        System.out.println("消费者: " + orderEvent.getValue());

        // 也可以用以下方式
        this.onEvent(orderEvent);
    }

    @Override
    public void onEvent(OrderEvent event) throws Exception {
//        SleepUtil.sleep(1, TimeUnit.HOURS);
        System.out.println("消费者: " + event.getValue());
    }
}
