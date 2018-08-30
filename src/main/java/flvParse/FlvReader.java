package flvParse;

import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

public class FlvReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlvReader.class);

    private ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;
    private File file;

    private FlvHeader header;

    private List<FlvTag> tags;

    public static boolean isFlvFile(ByteBuffer byteBuffer) {
        return byteBuffer.get(0) == 'F' && byteBuffer.get(1) == 'L' && byteBuffer.get(2) == 'V';
    }

    public FlvReader(File file) {
        this.file = file;
        parse();
    }

    private void parse() {
        try {
            FileChannel channel = new RandomAccessFile(file, "r").getChannel();
            ByteBuffer header = ByteBuffer.allocate(9);
            channel.read(header);
            if (!isFlvFile(header)) {
                System.out.println("file is not flv");
                return;
            }

            this.header = new FlvHeader(allocator.buffer(9).writeBytes(header));

            //跳过previousTagSize
            channel.position(channel.position() + 4);
            while (channel.position() < channel.size()) {
                CompositeByteBuf tag = allocator.compositeBuffer();

                ByteBuffer tagHeader = ByteBuffer.allocate(11);
                channel.read(tagHeader);
                tag.addComponent(allocator.buffer(11).writeBytes(tagHeader));

                int tagDataSize = tag.getMedium(1);
                //data
                ByteBuffer data = ByteBuffer.allocate(tagDataSize);
                channel.read(data);

                tag.addComponent(allocator.buffer(tagDataSize).writeBytes(data));

                tags.add(new FlvTag(tag));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FlvReader reader = new FlvReader(new File("/home/las/test.flv"));
        reader.tags.stream().forEach(x -> {
            LOGGER.info("tag {} ", x);
        });
    }
}
