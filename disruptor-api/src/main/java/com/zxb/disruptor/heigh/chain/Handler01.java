package com.zxb.disruptor.heigh.chain;

import com.lmax.disruptor.EventHandler;
import com.zxb.disruptor.common.SleepUtil;

import java.util.concurrent.TimeUnit;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2019-11-28 11:14
 */
public class Handler01 implements EventHandler<Trade> {

    @Override
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        event.setName("Handler01");
        System.out.println("handler01 set name");
        SleepUtil.sleep(1, TimeUnit.SECONDS);
    }
}
