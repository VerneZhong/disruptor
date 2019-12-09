package com.zxb.client;

import com.zxb.server.disruptor.MessageConsumer;
import com.zxb.server.entity.TranslatorData;
import com.zxb.server.entity.TranslatorDataWrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2019-12-09 09:27
 */
public class DefaultMessageConsumerClient extends MessageConsumer {

    public DefaultMessageConsumerClient(String consumerId) {
        super(consumerId);
    }

    @Override
    public void onEvent(TranslatorDataWrapper event) throws Exception {
        TranslatorData data = event.getData();
        ChannelHandlerContext handlerContext = event.getHandlerContext();

        // 业务逻辑处理
        try {
            System.out.println("DefaultMessageConsumerClient: " + data);
        } finally {
            ReferenceCountUtil.release(event);
        }
    }
}
