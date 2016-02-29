package buffer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class ByteBufferTest {

	public static void main(String[] args) throws Exception {
		ByteBufferTest b = new ByteBufferTest();
		b.test1();
//		b.test2();
	}

	private void test1() throws UnsupportedEncodingException {
		String str = "0123456789|0123456789|0123456789|0123456789|0123456789| /n";
		ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
		byteBuffer.put(str.getBytes());
		byteBuffer.flip();
		byte[] bytes = new byte[byteBuffer.remaining()];
		byteBuffer.get(bytes);
		System.out.println(new String(bytes));
	}

	private void test2() throws IOException {
		// 创建一个capacity为256的ByteBuffer
		ByteBuffer buf = ByteBuffer.allocate(256);
		while (true) {
			// 从标准输入流读入一个字符
			int c = System.in.read();
			// 当读到输入流结束时，退出循环
			if (c == -1)
				break;
			// 把读入的字符写入ByteBuffer中
			buf.put((byte) c);
			// 当读完一行时，输出收集的字符
			if (c == '\n') {
				// 调用flip()使limit变为当前的position的值,position变为0,
				// 为接下来从ByteBuffer读取做准备
				buf.flip();
				// 构建一个byte数组
				byte[] content = new byte[buf.limit()];
				// 从ByteBuffer中读取数据到byte数组中
				buf.get(content);
				// 把byte数组的内容写到标准输出
				System.out.print(new String(content));
				// 调用clear()使position变为0,limit变为capacity的值，
				// 为接下来写入数据到ByteBuffer中做准备
				buf.clear();
			}
		}
	}
}
