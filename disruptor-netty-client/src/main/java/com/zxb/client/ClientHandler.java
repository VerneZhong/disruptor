package com.zxb.client;


import com.zxb.server.disruptor.MessageProducer;
import com.zxb.server.disruptor.RingBufferWorkerPoolFactory;
import com.zxb.server.entity.TranslatorData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * 数据处理类
 * @author Mr.zxb
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//    	try {
//    		TranslatorData response = (TranslatorData)msg;
//    		System.err.println("Client端: id= " + response.getId()
//    				+ ", name= " + response.getName()
//    				+ ", message= " + response.getMessage());
//		} finally {
//			//一定要注意 用完了缓存 要进行释放
//			ReferenceCountUtil.release(msg);
//		}

		TranslatorData response = (TranslatorData)msg;

		String producerId = "code:sessionId:002";
		MessageProducer messageProducer = RingBufferWorkerPoolFactory.newInstance().getMessageProducer(producerId);
		messageProducer.onData(response, ctx);
	}
}
