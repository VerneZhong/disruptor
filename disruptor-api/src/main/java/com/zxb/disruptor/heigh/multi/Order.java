package com.zxb.disruptor.heigh.multi;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Event交易对象
 *
 * @author Mr.zxb
 * @date 2019-11-28 10:03
 */
@Data
public class Order {

    private String id;
    private String name;
    private Double price;

}
