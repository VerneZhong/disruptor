package com.zxb.disruptor.heigh.chain;

import javafx.event.Event;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Event交易对象
 *
 * @author Mr.zxb
 * @date 2019-11-28 10:03
 */
@Data
public class Trade {

    private String id;
    private String name;
    private Double price;
    private AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet();
    }
}
