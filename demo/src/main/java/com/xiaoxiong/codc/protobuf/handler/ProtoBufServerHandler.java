package com.xiaoxiong.codc.protobuf.handler;

import com.xiaoxiong.codc.protobuf.pojo.StudentPOJO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiongliang
 * @version 1.0
 * @description 服务器端处理器
 * @since 2022/3/28  21:32
 */
public class ProtoBufServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProtoBufServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        StudentPOJO.Student student = (StudentPOJO.Student) msg;
        LOGGER.info("msg:{}",student);
        StudentPOJO.Student ss = StudentPOJO.Student.newBuilder().setId(1000).setName("我是服务器").build();
        ctx.writeAndFlush(ss);
    }
}
