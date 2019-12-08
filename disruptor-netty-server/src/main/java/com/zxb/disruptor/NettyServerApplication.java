package com.zxb.disruptor;

import com.zxb.disruptor.server.NettyServer;
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

        new NettyServer();
    }
}
