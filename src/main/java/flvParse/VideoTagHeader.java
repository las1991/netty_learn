package flvParse;

import io.netty.buffer.ByteBuf;

public class VideoTagHeader {

    enum FrameType {
        NONE,
        //key frame (for AVC, a seekable frame)
        KEY,
        // inter frame (for AVC, a non-seekable frame)
        INTER,
        //disposable inter frame (H.263 only)
        DISPOSABLE_INTER,
        //generated key frame (reserved for server use only)
        GENERATED_KEY,
        //video info/command frame
        VIDEO_INFO_COMMAND
    }

    enum CodecId {
        NONE,
        NONE1,
        SORENSON_H263,
        SCREEN_VIDEO,
        ON2_VP6,
        ON2_VP6_ALPHA_CHANNEL,
        SCREEN_VIDEO_VERSION2,
        AVC
    }

    enum AVCPacketType {
        SEQUENCE_HEADER,
        NALU,
        END_OF_SEQUENCE
    }

    private FrameType frameType;
    private CodecId codecId;
    private AVCPacketType avcPacketType;
    private long compositionTime;

    public VideoTagHeader(ByteBuf content) {
        try {
            byte b0 = content.readByte();
            int frameType = (b0 & 0xf0) >> 4;
            this.frameType = FrameType.values()[frameType];

            int codecId = b0 & 0x0f;
            this.codecId = CodecId.values()[codecId];

            if (codecId == 7) {
                short packetType = content.readUnsignedByte();
                this.avcPacketType = AVCPacketType.values()[packetType];
                this.compositionTime = content.readMedium();
            }
        } finally {
            content.release();
        }

    }

    @Override
    public String toString() {
        return "VideoTagHeader{" +
                "frameType=" + frameType +
                ", codecId=" + codecId +
                ", avcPacketType=" + avcPacketType +
                ", compositionTime=" + compositionTime +
                '}';
    }
}
