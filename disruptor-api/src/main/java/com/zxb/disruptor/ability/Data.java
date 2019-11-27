package com.zxb.disruptor.ability;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2019-11-26 17:36
 */
@lombok.Data
public class Data {

    private Long id;
    private String name;

    public Data() {
    }

    public Data(Long id, String name) {
        this.id = id;
        this.name = name;
    }


}
