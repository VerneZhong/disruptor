package com.zxb.disruptor.zookeeper;

/**
 * Other classes use the DataMonitor by implementing this method
 *
 * @author Mr.zxb
 * @date 2019-12-02 10:55
 */
public interface DataMonitorListener {

    /**
     *  节点的存在状态已更改
     * @param bytes
     */
    void exists(byte[] bytes);

    /**
     * ZooKeeper会话不再有效
     * @param rc
     */
    void closing(int rc);
}
