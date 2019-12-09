package com.zxb.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import com.zxb.disruptor.server.DefaultMessageConsumerServer;
import com.zxb.disruptor.server.NettyServer;
import com.zxb.server.disruptor.MessageConsumer;
import com.zxb.server.disruptor.RingBufferWorkerPoolFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2019-12-07 18:01
 */
@SpringBootApplication
public class NettyServerApplication {

    public static void main(String[] args) {

        SpringApplication.run(NettyServerApplication.class, args);

        MessageConsumer[] messageConsumers = new MessageConsumer[4];
        for (int i = 0; i < messageConsumers.length; i++) {
            messageConsumers[i] = new DefaultMessageConsumerServer("code:serverId: " + i);
        }

        // 启动Disruptor
        RingBufferWorkerPoolFactory.newInstance().initAndStart(ProducerType.MULTI, 1024 * 1024,
                new YieldingWaitStrategy(),
                messageConsumers);

        // 启动Netty Server
        new NettyServer();

    }
}
