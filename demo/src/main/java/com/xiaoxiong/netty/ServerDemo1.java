package com.xiaoxiong.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

import java.net.InetSocketAddress;

/**
 * @author xiongliang
 * @version 1.0
 * @since 2022/1/25 10:39
 * netty 构建的服务器
 */
public class ServerDemo1 {

    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup(1),
                        new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() - 1))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new StringDecoder());
                        channel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(msg);
                            }
                        });
                    }
                })
                .bind(new InetSocketAddress(9090));

    }

}
