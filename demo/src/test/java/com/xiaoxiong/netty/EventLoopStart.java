package com.xiaoxiong.netty;

import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @author xiongliang
 * @version 1.0
 * @since 2022/2/6 19:31
 * netty 任务提交、执行流程
 */
public class EventLoopStart {

    public static void main(String[] args) {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        worker.execute(() ->
            System.out.println("Hello world!")
        );

    }

}
