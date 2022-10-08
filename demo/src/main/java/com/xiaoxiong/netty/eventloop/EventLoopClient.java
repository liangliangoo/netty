package com.xiaoxiong.netty.eventloop;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author xiongliang
 * @version 1.0
 * @since 2022/1/25 10:47
 * 客户端
 */
public class EventLoopClient {

    public static void main(String[] args) throws InterruptedException {
        Channel channel = new Bootstrap()
                .group(new NioEventLoopGroup(2))
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect("localhost", 9091)
                .sync()
                .channel();
        System.out.println(channel);
    }

}
