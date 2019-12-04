package com.zxb.disruptor.concurrent;

import com.zxb.disruptor.common.SleepUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2019-12-03 16:10
 */
public class CountDownLatchOnAqs {

    private Sync sync;

    public CountDownLatchOnAqs(int count) {
         this.sync = new Sync(count);
    }

    static class Sync extends AbstractQueuedSynchronizer {
        public Sync(int count) {
            setState(count);
        }

        @Override
        protected int tryAcquireShared(int arg) {
            return getCount() == 0 ? 1 : -1;
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            for (; ; ) {
                int state = getState();
                // 多线程环境下，当state为0直接返回
                if (state == 0) {
                    return true;
                }
                int c = state - arg;
                if (compareAndSetState(state, c)) {
                    return c == 0;
                }
            }
        }

        public int getCount() {
            return getState();
        }

    }

    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    public void countDown() {
        sync.releaseShared(1);
    }

    public int getCount() {
       return sync.getCount();
    }

    public static void main(String[] args) throws InterruptedException {

        CountDownLatchOnAqs countDownLatchOnAqs = new CountDownLatchOnAqs(2);

        new Thread(() -> {
            System.out.println(Thread.currentThread() + "await");
            try {
                countDownLatchOnAqs.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();


        new Thread(() -> {
            SleepUtil.sleep(1, TimeUnit.SECONDS);
            countDownLatchOnAqs.countDown();
            System.out.println(Thread.currentThread() + "countDown");
        }).start();

        new Thread(() -> {
            SleepUtil.sleep(2, TimeUnit.SECONDS);
            countDownLatchOnAqs.countDown();
            System.out.println(Thread.currentThread() + "countDown");
        }).start();

        System.out.println("Main thread await");
        countDownLatchOnAqs.await();
        SleepUtil.sleep(1, TimeUnit.SECONDS);
        System.out.println("Main thread over");
    }

}

