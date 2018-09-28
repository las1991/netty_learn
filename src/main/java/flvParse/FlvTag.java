package flvParse;

import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlvTag {

    private final static Logger LOGGER = LoggerFactory.getLogger(FlvTag.class);

    enum TagType {
        AUDIO, VIDEO, SCRIPT_DATA
    }

    private int reserved;
    private boolean isfilter;
    private TagType tagType;
    private int dataSize;
    private long timeStamp;
    private int streamId;

    private AudioTagHeader audioTagHeader;
    private VideoTagHeader videoTagHeader;

    public FlvTag(ByteBuf content) {
        try {
            if (content.readableBytes() < 11) {
                throw new IllegalArgumentException("is not flv tag");
            }
            byte b = content.readByte();
            //1100 0000
            this.reserved = b & 0xc0;
            //0010 0000
            this.isfilter = (b & 0x20) > 0;
            //0001 1111

            switch (b & 0x1f) {
                case 0x08:
                    this.tagType = TagType.AUDIO;
                    break;
                case 0x09:
                    this.tagType = TagType.VIDEO;
                    break;
                case 0x12:
                    this.tagType = TagType.SCRIPT_DATA;
                    break;
                default:
                    LOGGER.info(" type {}", b & 0x1f);
            }

            this.dataSize = content.readMedium();
            this.timeStamp = content.readMedium() & (content.readByte() << 24);
            this.streamId = content.readByte();

            switch (this.tagType) {
                case AUDIO:
                    this.audioTagHeader = new AudioTagHeader(content.readSlice(content.readableBytes()).retain());
                    break;
                case VIDEO:
                    this.videoTagHeader=new VideoTagHeader(content.readSlice(content.readableBytes()).retain());
                    break;
                case SCRIPT_DATA:
                    break;
                default:
            }
        } catch (Exception e) {
            LOGGER.error("{}", e.getMessage(), e);
        } finally {
            content.release();
        }
    }

    public int getReserved() {
        return reserved;
    }

    public void setReserved(int reserved) {
        this.reserved = reserved;
    }

    public boolean isIsfilter() {
        return isfilter;
    }

    public void setIsfilter(boolean isfilter) {
        this.isfilter = isfilter;
    }

    public TagType getTagType() {
        return tagType;
    }

    public void setTagType(TagType tagType) {
        this.tagType = tagType;
    }

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getStreamId() {
        return streamId;
    }

    public void setStreamId(int streamId) {
        this.streamId = streamId;
    }

    public AudioTagHeader getAudioTagHeader() {
        return audioTagHeader;
    }

    public void setAudioTagHeader(AudioTagHeader audioTagHeader) {
        this.audioTagHeader = audioTagHeader;
    }

    public VideoTagHeader getVideoTagHeader() {
        return videoTagHeader;
    }

    public void setVideoTagHeader(VideoTagHeader videoTagHeader) {
        this.videoTagHeader = videoTagHeader;
    }

    @Override
    public String toString() {
        return "FlvTag{" +
                "reserved=" + reserved +
                ", isfilter=" + isfilter +
                ", tagType=" + tagType +
                ", dataSize=" + dataSize +
                ", timeStamp=" + timeStamp +
                ", streamId=" + streamId +
                ", audioTagHeader=" + audioTagHeader +
                ", videoTagHeader=" + videoTagHeader +
                '}';
    }
}
