package com.zxb.disruptor.quickstart;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.Executors;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2019-11-26 22:42
 */
public class Main {

    public static void main(String[] args) {
        int ringBufferSize = 1024 * 1024;

        /**
         * 1 EventFactory：消息（event）工厂对象
         * 2 ringBufferSize：容器的长度
         * 3 executor: 线程池（建议使用自定义线程池） RejectedExecutionHandler
         * 4 ProducerType： 单生产者还是多生产者
         * 5 waitStrategy:  等待策略
         */

        // 1、实例化Disruptor对象
        Disruptor<OrderEvent> disruptor = new Disruptor<>(new OrderEventFactory(),
                ringBufferSize,
                Executors.defaultThreadFactory(),
                ProducerType.SINGLE,
                new BlockingWaitStrategy());

        // 2、添加消费者的监听 (disruptor 与消费者的一个关联关系)
        disruptor.handleEventsWith(new OrderEventHandler());

        // 3、启动disruptor
        disruptor.start();
    }

}
