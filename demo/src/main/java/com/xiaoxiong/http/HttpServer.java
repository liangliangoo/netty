package com.xiaoxiong.http;

import com.xiaoxiong.http.handler.HttpServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author xiongliang
 * @version 1.0
 * @description http服务器
 * @since 2022/3/28  9:28
 */
public class HttpServer {

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(2);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(workerGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class)
                    .childHandler(new HttpServerChannelInitializer());

            ChannelFuture channelFuture = bootstrap.bind(9090).sync();
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
