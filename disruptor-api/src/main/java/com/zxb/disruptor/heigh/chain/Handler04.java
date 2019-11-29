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
public class Handler04 implements EventHandler<Trade> {

    @Override
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        event.increment();
        System.out.println("handler04 set count++");
        SleepUtil.sleep(1, TimeUnit.SECONDS);
    }
}
