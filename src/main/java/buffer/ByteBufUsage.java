package buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.    charset.Charset;

/**
 * Created by las on 2017/2/21.
 */
public class ByteBufUsage {

    public void handleArray(byte[] bytes, int offset, int length) {
        System.out.println("offset:[" + offset + "],length:[" + length + "]");
        for (int i = offset; i < length + offset; i++) {
            System.out.println(bytes[i]);
        }
    }

    public void writeMessage(ByteBuf byteBuf) {
        for (int i = 0; i < Math.min(10, (byteBuf.capacity() >> 1)); i++) {
            byteBuf.writeByte(i);
            byteBuf.writeChar(i);
        }

    }

    public void writeMessage(ByteBuffer byteBuffer) {
        for (int i = 0; i < byteBuffer.remaining(); i++) {
            byteBuffer.putInt(i);
            byteBuffer.putChar((char) i);
        }
    }

    @Test
    public void backingArray() {
        ByteBuf heapBuf = Unpooled.buffer();
        writeMessage(heapBuf);
        if (heapBuf.hasArray()) {
            byte[] bytes = heapBuf.array();
            int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();
            int length = heapBuf.readableBytes();
            handleArray(bytes, offset, length);
        }
    }

    @Test
    public void directBufferDataAccess() {
        ByteBuf directBuffer = Unpooled.directBuffer();
        writeMessage(directBuffer);
        if (!directBuffer.hasArray()) {
            int length = directBuffer.readableBytes();
            byte[] bytes = new byte[length];
            directBuffer.getBytes(directBuffer.readerIndex(), bytes);
            handleArray(bytes, 0, length);
        }
    }

    @Test
    public void composite2ByteBuffer() {
        ByteBuffer header = ByteBuffer.allocate(15);
        writeMessage(header);
        ByteBuffer body = ByteBuffer.allocateDirect(20);
        writeMessage(body);
        // Use an array to hold the message parts
        ByteBuffer[] message = new ByteBuffer[]{header, body};
// Create a new ByteBuffer and use copy to merge the header and body
        ByteBuffer message2 =
                ByteBuffer.allocate(header.remaining() + body.remaining());
        message2.put(header);
        message2.put(body);
        message2.flip();
    }

    @Test
    public void composite2ByteBuf() {
        CompositeByteBuf messageBuf = Unpooled.compositeBuffer();
        ByteBuf headerBuf = Unpooled.buffer(); // can be backing or direct
        writeMessage(headerBuf);
        ByteBuf bodyBuf = Unpooled.directBuffer();   // can be backing or direct
        writeMessage(bodyBuf);
        messageBuf.addComponents(headerBuf, bodyBuf);
        messageBuf.removeComponent(0);// remove the header
    }

    @Test
    public void accessCompositeByteBuf() {
        CompositeByteBuf compBuf = Unpooled.compositeBuffer();
        writeMessage(compBuf);
        int length = compBuf.readableBytes();
        byte[] array = new byte[length];
        compBuf.getBytes(compBuf.readerIndex(), array);
        handleArray(array, 0, array.length);
    }

    @Test
    public void sliceByteBuf() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        ByteBuf sliced = buf.slice(0, 14);
        System.out.println(sliced.toString(utf8));
        buf.setByte(0, (byte) 'J');
        assert buf.getByte(0) == sliced.getByte(0);
    }

    @Test
    public void copyByteBuf() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        ByteBuf copy = buf.copy(0, 14);
        System.out.println(copy.toString(utf8));
        buf.setByte(0, (byte) 'J');
        assert buf.getByte(0) != copy.getByte(0);
    }

    @Test
    public void getAndSet() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        System.out.println((char) buf.getByte(0));
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();
        buf.setByte(0, (byte) 'B');
        System.out.println((char) buf.getByte(0));
        assert readerIndex == buf.readerIndex();
        assert writerIndex == buf.writerIndex();
    }

    @Test
    public void readAndWrite() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();
        System.out.println((char) buf.readByte());
        buf.writeByte((byte) '?');
        assert readerIndex != buf.readerIndex();
        assert writerIndex != buf.writerIndex();
    }
}
