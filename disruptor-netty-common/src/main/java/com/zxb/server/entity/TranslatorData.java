package com.zxb.server.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2019-12-07 18:19
 */
@Data
public class TranslatorData implements Serializable {

    private String id;
    private String name;

    /**
     * 传输消息内容
     */
    private String message;
}
