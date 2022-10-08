package com.xiaoxiong.websocket;

import com.xiaoxiong.websocket.handler.TextWebSocketFrameHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author xiongliang
 * @version 1.0
 * @description websocket server
 * @since 2022/3/28  15:04
 */
public class WebSocketServer {

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(2);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        try {
            serverBootstrap.group(workerGroup, workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new HttpServerCodec());
                    // 以数据块的方式写
                    pipeline.addLast(new ChunkedWriteHandler());
                    // 数据段聚合器
                    pipeline.addLast(new HttpObjectAggregator(8192));
                    // websocket 数据是以帧的形式传递 这个处理器会将http协议升级为ws
                    pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
                    // 处理业务逻辑
                    pipeline.addLast(new TextWebSocketFrameHandler());
                }
            });
            ChannelFuture channelFuture = serverBootstrap.bind(9090).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

}
