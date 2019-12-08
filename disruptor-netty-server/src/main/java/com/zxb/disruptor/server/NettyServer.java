package com.zxb.disruptor.server;

import com.zxb.server.util.MarshallingCodecFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Netty Server
 * @author Mr.zxb
 * @date 2019-12-07 18:01
 */
public class NettyServer {

	public static final int PORT = 8765;

	public NettyServer() {
		//1. 创建两个工作线程组: 一个用于接受网络请求的线程组. 另一个用于实际处理业务的线程组
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		//2 辅助类
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		try {
			serverBootstrap.group(bossGroup, workGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 1024)
			//表示缓存区动态调配（自适应）
			.option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator())
			//缓存区 池化操作
			.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel sc) throws Exception {
					sc.pipeline()
							.addLast(MarshallingCodecFactory.buildMarshallingDecoder())
							.addLast(MarshallingCodecFactory.buildMarshallingEncoder())
							.addLast(new ServerHandler());
				}
			});
			//绑定端口，同步等等请求连接
			ChannelFuture cf = serverBootstrap.bind(PORT).sync();
			System.err.println("Server Startup...");
			cf.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			//优雅停机
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
			System.err.println("Sever ShutDown...");
		}
	}
	
}
