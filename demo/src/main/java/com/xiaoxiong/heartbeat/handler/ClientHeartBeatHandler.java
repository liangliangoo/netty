package com.xiaoxiong.heartbeat.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author xiongliang
 * @version 1.0
 * @since 2022/2/5 13:30
 */
public class ClientHeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ping(ctx);
            switch (((IdleStateEvent) evt).state()) {
                case READER_IDLE:
                    System.out.println("reader idle");
                    break;
                case WRITER_IDLE:
                    System.out.println("writer idle");
                    break;
                case ALL_IDLE:
                    System.out.println("all idle");
                    break;
            }
        } else {
            ctx.fireUserEventTriggered(evt);
        }
    }

    private void ping(ChannelHandlerContext ctx) {
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes("ping".getBytes());
        ctx.writeAndFlush(buffer);
    }


}
