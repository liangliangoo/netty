package com.xiaoxiong.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xiongliang
 * @version 1.0
 * @since 2022/1/22 19:50
 * AIO服务器
 */
public class AioServer {

    private final int port;

    private ExecutorService threadPool = Executors.newCachedThreadPool();

    public AioServer(int port) {
        this.port = port;
        listen();
    }

    public static void main(String[] args) {
        int port = 8080;
        new AioServer(port);
    }

    private void listen() {
        try {
            AsynchronousChannelGroup group = AsynchronousChannelGroup
                    .withCachedThreadPool(threadPool, 1);
            final AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel
                    .open(group);
            server.bind(new InetSocketAddress(port));
            System.out.println("服务器已启动，暴露的port是" + port);

            server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {

                // 这里分配的内存空间是堆外的内存
                final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
                /**
                 * Invoked when an operation has completed.
                 *
                 * @param result     The result of the I/O operation.
                 * @param attachment
                 */
                @Override
                public void completed(AsynchronousSocketChannel result, Object attachment) {
                    System.out.println("I/O 操作完成,开始获取数据");
                    try {
                        buffer.clear();
                        // 将通道中读取的数据存到缓冲区中
                        result.read(buffer);

                        buffer.flip();
                        // 将缓冲区中的字节序列写到通道中
                        result.write(buffer);
                        buffer.flip();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            result.close();
                            server.accept(null, this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("操作完成");
                }

                /**
                 * Invoked when an operation fails.
                 *
                 * @param exc        The exception to indicate why the I/O operation failed
                 * @param attachment
                 */
                @Override
                public void failed(Throwable exc, Object attachment) {
                    System.out.println(exc.getMessage());
                }
            });

            try {
                Thread.sleep(Integer.MAX_VALUE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
