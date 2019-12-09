package com.zxb.disruptor.server;

import com.zxb.server.disruptor.MessageConsumer;
import com.zxb.server.entity.TranslatorData;
import com.zxb.server.entity.TranslatorDataWrapper;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2019-12-09 09:20
 */
public class DefaultMessageConsumerServer extends MessageConsumer {

	public DefaultMessageConsumerServer(String consumerId) {
		super(consumerId);
	}

	@Override
    public void onEvent(TranslatorDataWrapper event) throws Exception {
        TranslatorData request = event.getData();
        // 1. 实际业务处理
        System.err.println("Sever端: id= " + request.getId()
    					+ ", name= " + request.getName()
    					+ ", message= " + request.getMessage());

    	// 2. 回送响应信息
    	TranslatorData response = new TranslatorData();
    	response.setId("resp: " + request.getId());
    	response.setName("resp: " + request.getName());
    	response.setMessage("resp: " + request.getMessage());
    	// 写出response响应信息:
    	event.getHandlerContext().writeAndFlush(response);
    }
}
