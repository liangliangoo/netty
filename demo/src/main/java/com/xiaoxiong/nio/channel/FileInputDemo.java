package com.xiaoxiong.nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author xiongliang
 * @version 1.0
 * @since 2022/1/23 18:32
 */
public class FileInputDemo {

    public static void main(String[] args) throws Exception {
        FileInputStream fis = new FileInputStream(new File("E://text.txt"));
        FileChannel channel = fis.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer);
        buffer.flip();
        System.out.println(new String(buffer.array()));
        fis.close();
    }

}
