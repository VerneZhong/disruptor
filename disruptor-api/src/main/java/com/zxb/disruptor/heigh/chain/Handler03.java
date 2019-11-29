package com.zxb.disruptor.heigh.chain;

import com.lmax.disruptor.EventHandler;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2019-11-28 11:14
 */
public class Handler03 implements EventHandler<Trade> {

    @Override
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("handler03 event = " + event);
    }
}
