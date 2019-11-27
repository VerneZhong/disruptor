package com.zxb.disruptor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Disruptor Quick Start
 * 1、建立一个工厂Event类，用于创建Event类实例对象
 * 2、需要有一个监听事件类，用于处理数据（Event类）
 * 3、实例化Disruptor实例，配置一系列参数，编写Disruptor核心组件
 * 4、编写生产者组件，向Disruptor容器中去投递数据
 *
 * @author Mr.zxb
 * @date 2019-11-26 22:27
 */
@SpringBootApplication
public class DisruptorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DisruptorApplication.class, args);
    }
}
