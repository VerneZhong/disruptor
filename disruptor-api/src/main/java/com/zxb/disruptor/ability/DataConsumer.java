package com.zxb.disruptor.ability;

import com.lmax.disruptor.EventHandler;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2019-11-26 21:38
 */
public class DataConsumer implements EventHandler<Data> {

    private long startTime;
    private int i;

    public DataConsumer() {
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void onEvent(Data data, long l, boolean b) throws Exception {
        i++;
        if (i == Constants.EVENT_NUM_FM) {
            long endTime = System.currentTimeMillis();
            System.out.println("Disruptor costTime = " + (endTime - startTime) + "ms");
        }
    }
}
