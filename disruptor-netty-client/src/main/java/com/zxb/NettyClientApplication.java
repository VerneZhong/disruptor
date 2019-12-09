package com.zxb;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import com.zxb.client.DefaultMessageConsumerClient;
import com.zxb.client.NettyClient;
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
public class NettyClientApplication {

    public static void main(String[] args) {

        SpringApplication.run(NettyClientApplication.class, args);

        MessageConsumer[] messageConsumers = new MessageConsumer[4];
        for (int i = 0; i < messageConsumers.length; i++) {
            messageConsumers[i] = new DefaultMessageConsumerClient("code:clientId: " + i);
        }

        // 启动Disruptor
        RingBufferWorkerPoolFactory.newInstance().initAndStart(ProducerType.MULTI, 1024 * 1024,
                new BlockingWaitStrategy(),
                messageConsumers);

        // 建立连接并发送消息
        NettyClient nettyClient = new NettyClient();
        nettyClient.sendData();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                nettyClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }
}
