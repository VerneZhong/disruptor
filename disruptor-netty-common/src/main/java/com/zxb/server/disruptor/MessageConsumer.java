package com.zxb.server.disruptor;

import com.lmax.disruptor.WorkHandler;
import com.zxb.server.entity.TranslatorDataWrapper;
import lombok.Data;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2019-12-08 15:59
 */
@Data
public abstract class MessageConsumer implements WorkHandler<TranslatorDataWrapper> {

    protected String consumerId;

    public MessageConsumer(String consumerId) {
        this.consumerId = consumerId;
    }
}
