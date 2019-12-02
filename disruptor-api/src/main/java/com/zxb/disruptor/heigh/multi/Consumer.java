package com.zxb.disruptor.heigh.multi;

import com.lmax.disruptor.WorkHandler;
import com.zxb.disruptor.common.SleepUtil;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多消费者实现类
 *
 * @author Mr.zxb
 * @date 2019-11-29 14:10
 */
public class Consumer implements WorkHandler<Order> {

    private String id;

    public static final AtomicInteger COUNT = new AtomicInteger(0);

    private Random random;

    public Consumer(String id) {
        this.id = id;
        this.random = new Random();
    }

    @Override
    public void onEvent(Order event) throws Exception {
        SleepUtil.sleep(1 * random.nextInt(5), TimeUnit.MILLISECONDS);
        System.out.println("消费者：" + id + ", 消费信息ID：" + event.getId());
        COUNT.incrementAndGet();
    }

    public int getCount() {
        return COUNT.get();
    }
}
