package com.zxb.disruptor.common;

import java.util.concurrent.TimeUnit;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2019-11-28 10:37
 */
public class SleepUtil {

    public static void sleep(long time, TimeUnit unit) {
        try {
            unit.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
