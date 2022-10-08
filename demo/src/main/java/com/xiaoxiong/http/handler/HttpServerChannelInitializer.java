package com.xiaoxiong.http.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author xiongliang
 * @version 1.0
 * @description channel初始化器
 * @since 2022/3/28  9:35
 */
public class HttpServerChannelInitializer extends ChannelInitializer<NioSocketChannel> {


    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());
        pipeline.addLast("HttpServerChannelInitializer",new HttpServerInBoundHandler());
    }


}
