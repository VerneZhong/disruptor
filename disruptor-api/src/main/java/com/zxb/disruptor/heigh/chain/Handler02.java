package com.zxb.disruptor.heigh.chain;

import com.lmax.disruptor.EventHandler;
import com.zxb.disruptor.common.SleepUtil;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2019-11-28 11:14
 */
public class Handler02 implements EventHandler<Trade> {

    @Override
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        event.setId(UUID.randomUUID().toString());
        System.out.println("handler02 set id");
        SleepUtil.sleep(2, TimeUnit.SECONDS);
    }
}
