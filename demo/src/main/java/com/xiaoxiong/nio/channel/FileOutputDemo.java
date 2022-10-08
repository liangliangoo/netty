package com.xiaoxiong.nio.channel;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author xiongliang
 * @version 1.0
 * @since 2022/1/23 17:52
 * 使用NIO将数据写入文件
 */
public class FileOutputDemo {

    // 模拟的一些数据
    private static final byte[] message = {83, 111, 109, 101, 32, 98,
            121, 116, 101, 115, 46};

    public static void main(String[] args) throws Exception {

        FileOutputStream fos = new FileOutputStream("E://text.txt");
        FileChannel channel = fos.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        for (int i = 0; i < message.length; i++) {
            buffer.put(message[i]);
        }

        buffer.flip();

        channel.write(buffer);

        fos.close();
    }

}
