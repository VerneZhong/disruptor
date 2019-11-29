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
public class Handler05 implements EventHandler<Trade> {

    @Override
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("handler05 set price");
        event.setPrice(200D);
        SleepUtil.sleep(1, TimeUnit.SECONDS);
    }
}
