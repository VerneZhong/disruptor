package com.zxb.server.entity;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2019-12-08 16:07
 */
@Data
public class TranslatorDataWrapper {

    private TranslatorData data;

    private ChannelHandlerContext handlerContext;
}
