package test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;


/**
 * Created by las on 2017/6/12.
 */
public class TestByteBuffer {
    static byte[] CONTENT = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};

    public static void main(String[] args) {
        int loop = 300 * 10000;
        ByteBuf buffer = null;
        long start1 = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            buffer = Unpooled.directBuffer(1024);
            buffer.writeBytes(CONTENT);
            buffer.release();
        }
        System.out.println("use :" + (System.currentTimeMillis() - start1));
        long start = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            buffer = PooledByteBufAllocator.DEFAULT.directBuffer(1024);
            buffer.writeBytes(CONTENT);
            buffer.release();
        }
        System.out.println("use :" + (System.currentTimeMillis() - start));


    }
}
