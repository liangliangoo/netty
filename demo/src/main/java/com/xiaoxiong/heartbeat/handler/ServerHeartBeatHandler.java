package com.xiaoxiong.heartbeat.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xiongliang
 * @version 1.0
 * @since 2022/2/5 13:14
 */
@Slf4j
public class ServerHeartBeatHandler extends ChannelInboundHandlerAdapter {

    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            counter.incrementAndGet();
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
            // more than three times,close channel
            if (counter.get() > 3) {
                log.info("close channel");
                ctx.channel().close();
            }
        } else {
            ctx.fireUserEventTriggered(evt);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        counter.set(0);
        ByteBuf buf = (ByteBuf) msg;
        String str = buf.toString(Charset.defaultCharset());
        if (str.equalsIgnoreCase("ping")) {
            buf.clear();
            buf.writeBytes("pong".getBytes());
            ctx.writeAndFlush(buf);
            buf.release();
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("establish connection");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("close connection");
    }
}
