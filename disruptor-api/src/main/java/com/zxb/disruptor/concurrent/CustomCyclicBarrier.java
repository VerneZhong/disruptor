package com.zxb.disruptor.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2019-12-04 15:57
 */
public class CustomCyclicBarrier {

    /**
     * 跨越屏障到数量
     */
    private int parties;

    /**
     * 当前数量
     */
    private int count;

    /**
     * 达到屏障后，执行的线程
     */
    private Runnable runnable;

    private ReentrantLock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    private Generation generation = new Generation();

    public static class Generation {
        boolean broken = false;
    }

    public CustomCyclicBarrier(int parties) {
        this(parties, null);
    }

    public CustomCyclicBarrier(int parties, Runnable runnable) {
        if (this.parties < 0) {
            throw new IllegalArgumentException("parties not less than 0");
        }
        this.parties = this.count = parties;
        this.runnable = runnable;
    }

    public int await() throws BrokenBarrierException {
        lock.lock();
        try {
            int index = --count;
            Generation g = generation;

            // 屏障已破
            if (g.broken) {
                throw new BrokenBarrierException();
            }
            boolean runAction = false;
            try {
                if (index == 0) {
                    if (runnable != null) {
                        runnable.run();
                    }
                    runAction = true;
                    nextGeneration();
                    return 0;
                }
            } catch (Exception e) {
                if (!runAction) {
                    breakBarrier();
                }
            }
            for (; ; ) {
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    breakBarrier();
                }
                if (g != generation) {
                    return index;
                }
            }
        } finally {
            lock.unlock();
        }
    }

    private void breakBarrier() {
        this.count = parties;
        condition.signalAll();
        generation.broken = true;
    }

    private void nextGeneration() {
        this.count = parties;
        condition.signalAll();
        generation = new Generation();
    }

    public void reset() {
        lock.lock();
        try {
            breakBarrier();
            nextGeneration();
        } finally {
            lock.unlock();
        }
    }

    public boolean isBroken() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return generation.broken;
        } finally {
            lock.unlock();
        }
    }

    public int getNumberWaiting() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return parties - count;
        } finally {
            lock.unlock();
        }
    }

}
