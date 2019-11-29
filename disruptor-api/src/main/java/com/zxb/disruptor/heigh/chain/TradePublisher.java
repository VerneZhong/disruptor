package com.zxb.disruptor.heigh.chain;

import com.lmax.disruptor.dsl.Disruptor;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2019-11-28 10:15
 */
public class TradePublisher implements Runnable {

    private CountDownLatch countDownLatch;
    private Disruptor<Trade> disruptor;
    private Random random;
    public static final int PUBLISHER_COUNT = 1;

    public TradePublisher(CountDownLatch countDownLatch, Disruptor<Trade> disruptor) {
        this.countDownLatch = countDownLatch;
        this.disruptor = disruptor;
        this.random = new Random();
    }

    @Override
    public void run() {
        for (int i = 0; i < PUBLISHER_COUNT; i++) {
            // 新的提交任务的方式
            disruptor.publishEvent(this::generateTrade);
        }
        countDownLatch.countDown();
    }

    private void generateTrade(Trade trade, long sequence) {
        trade.setPrice(random.nextDouble() * 9999);
    }

}
