package com.xiaoxiong.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

/**
 * @author xiongliang
 * @version 1.0
 * @since 2022/1/22 20:10
 */
public class AioClient {

    private final AsynchronousSocketChannel client;

    public AioClient() throws IOException {
        client = AsynchronousSocketChannel.open();
    }

    public void connect(String host, int port) {
        client.connect(new InetSocketAddress(host, port), null, new CompletionHandler<Void, Object>() {

            @Override
            public void completed(Void result, Object attachment) {
                try {
                    Integer integer = client.write(ByteBuffer.wrap("这是测试数据".getBytes(StandardCharsets.UTF_8))).get();
                    System.out.println("数据已发送");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                exc.printStackTrace();
            }
        });

        final ByteBuffer buffer = ByteBuffer.allocate(1024);
        client.read(buffer, null, new CompletionHandler<Integer, Object>() {
            @Override
            public void completed(Integer result, Object attachment) {
                System.out.println("IO完成" + result);
                System.out.println("获取返回结果===>" + new String(buffer.array(),StandardCharsets.UTF_8));
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                exc.printStackTrace();
            }
        });
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new AioClient().connect("127.0.0.1", 8080);
    }

}
