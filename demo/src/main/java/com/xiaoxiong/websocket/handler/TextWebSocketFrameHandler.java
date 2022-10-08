package com.xiaoxiong.websocket.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * @author xiongliang
 * @version 1.0
 * @description
 * @since 2022/3/28  15:21
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextWebSocketFrameHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

        LOGGER.info("收到的message:{}", msg.text());
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间:" + LocalDateTime.now() + msg.text()));

    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("handlerAdded 被调用，channel id：{}", ctx.channel().id().asLongText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("handlerRemoved 被调用，channel id：{}", ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.info("error cause by:{}", cause.getMessage());
        ctx.close();
    }

}
