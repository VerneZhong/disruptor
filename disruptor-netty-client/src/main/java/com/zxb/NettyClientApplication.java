package com.zxb;

import com.zxb.client.NettyClient;
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
