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
import java.util.ArrayList;
import java.util.List;

public class FlvReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlvReader.class);

    private ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;
    private File file;

    private FlvHeader header;

    private List<FlvTag> tags = new ArrayList<>();

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
            header.flip();

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
                tagHeader.flip();
                tag.addComponent(true, allocator.buffer(11).writeBytes(tagHeader));

                int tagDataSize = tag.getMedium(1);
                //data
                ByteBuffer data = ByteBuffer.allocate(tagDataSize);
                channel.read(data);
                data.flip();

                tag.addComponent(true, allocator.buffer(tagDataSize).writeBytes(data));

                tags.add(new FlvTag(tag));

                channel.position(channel.position() + 4);
            }
        } catch (IOException e) {
            LOGGER.error("{}", e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        String file = FlvReader.class.getResource("/test.flv").getPath();
        FlvReader reader = new FlvReader(new File(file));
        reader.tags.stream().forEach(x -> {
            LOGGER.info("tag {} ", x);
        });
    }
}
