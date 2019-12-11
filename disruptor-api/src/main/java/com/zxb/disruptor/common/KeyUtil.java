package com.zxb.disruptor.common;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2019-12-10 11:04
 */
public class KeyUtil {

    public static String generatorUUID() {
        TimeBasedGenerator timeBasedGenerator = Generators.timeBasedGenerator(EthernetAddress.fromInterface());
        return timeBasedGenerator.generate().toString();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(KeyUtil.generatorUUID());

        }
    }
}
