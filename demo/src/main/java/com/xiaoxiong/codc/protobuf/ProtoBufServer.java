package com.xiaoxiong.codc.protobuf;

import com.xiaoxiong.codc.protobuf.handler.ProtoBufServerHandler;
import com.xiaoxiong.codc.protobuf.pojo.StudentPOJO;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

/**
 * @author xiongliang
 * @version 1.0
 * @description 使用protobuf传输数据
 * @since 2022/3/28  20:25
 */
public class ProtoBufServer {

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(2);
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.group(workGroup, workGroup);
            bootstrap.channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 指定解码器，并指定对哪种对象解码
                            pipeline.addLast(new ProtobufDecoder(StudentPOJO.Student.getDefaultInstance()));
                            pipeline.addLast(new ProtoBufServerHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(9090).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

}
