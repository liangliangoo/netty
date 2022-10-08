package com.xiaoxiong.heartbeat;

import cn.hutool.core.thread.NamedThreadFactory;
import com.xiaoxiong.heartbeat.handler.ServerHeartBeatHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author xiongliang
 * @version 1.0
 * @since 2022/2/4 14:58
 *
 * netty tcp Server
 *
 */
@Slf4j
public class TcpServer {

    public static void main(String[] args) {

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        NioEventLoopGroup boss = new NioEventLoopGroup(1,
                new NamedThreadFactory("boss-thread", false));
        NioEventLoopGroup worker = new NioEventLoopGroup(6,
                new NamedThreadFactory("worker-thread", false));

        try {
            serverBootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new IdleStateHandler(2, 4, 10))
                                    .addLast(new ServerHeartBeatHandler());
                        }
                    });

            ChannelFuture future = serverBootstrap.bind(new InetSocketAddress("127.0.0.1", 9093));
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        } finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }


    }

}
