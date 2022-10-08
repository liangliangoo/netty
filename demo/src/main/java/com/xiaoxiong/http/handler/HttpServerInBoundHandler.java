package com.xiaoxiong.http.handler;

import cn.hutool.core.util.ReUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import kotlin.text.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

import static io.netty.handler.codec.http.HttpHeaderNames.*;

/**
 * @author xiongliang
 * @version 1.0
 * @description 入栈处理
 * @since 2022/3/28  9:41
 * HttpObject 是通讯的数据类型
 */
public class HttpServerInBoundHandler extends SimpleChannelInboundHandler<HttpObject> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServerInBoundHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            LOGGER.info("请求客户端地址:{}", ctx.channel().remoteAddress());

            HttpRequest httpRequest = (HttpRequest) msg;
            URI uri = new URI(httpRequest.uri());
            if (uri.getPath().equals("/favicon.ico")) {
                // nothing to do
                return;
            }

            //  回复响应
            ByteBuf content = Unpooled.copiedBuffer("I'm server", CharsetUtil.UTF_8);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,content);
            response.headers().set(CONTENT_TYPE, "text/plain");
            response.headers().set(CONTENT_LENGTH, content.readableBytes());
            ctx.writeAndFlush(response);
        }
    }

}
