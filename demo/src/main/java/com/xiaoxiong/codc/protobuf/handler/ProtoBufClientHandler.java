package com.xiaoxiong.codc.protobuf.handler;

import com.xiaoxiong.codc.protobuf.pojo.StudentPOJO;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

/**
 * @author xiongliang
 * @version 1.0
 * @description 客户端业务处理
 * @since 2022/3/28  21:09
 */
public class ProtoBufClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProtoBufClientHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        StudentPOJO.Student student = StudentPOJO.Student.newBuilder().setId(10).setName("我是客户端").build();
        ctx.channel().writeAndFlush(student);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.info("message : {}", msg.toString());
    }
}
