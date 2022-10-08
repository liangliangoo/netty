package com.xiaoxiong.netty.eventloop;

import cn.hutool.core.thread.NamedThreadFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @author xiongliang
 * @version 1.0
 * @since 2022/1/28 18:21
 * 将handler与 eventLoop绑定
 */
@Slf4j
public class EventLoopDemo1 {

    public static void main(String[] args) {

        NioEventLoopGroup boss = new NioEventLoopGroup(1, new NamedThreadFactory("boss", false));
        NioEventLoopGroup work = new NioEventLoopGroup(new NamedThreadFactory("worker", false));
        DefaultEventLoop defaultEventLoop = new DefaultEventLoop(new NamedThreadFactory("DefaultEventLoop", false));

        new ServerBootstrap()
                .group(boss, work)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){

                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf obj = (ByteBuf) msg;
                                log.info(obj.toString(Charset.defaultCharset()));
                                ctx.fireChannelRead(obj);
                            }

                            @Override
                            public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                                log.info("channelRegistered");
                            }
                        });
                        ch.pipeline().addLast(defaultEventLoop,"test",new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.info(msg.toString());
                            }
                        });
                    }
                })
                .bind(new InetSocketAddress("localhost", 9091));




    }


}
