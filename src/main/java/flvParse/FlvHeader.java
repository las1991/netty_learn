package flvParse;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;

public class FlvHeader extends DefaultByteBufHolder {
    private int version;
    private boolean hasAudio;
    private boolean hasVideo;

    public FlvHeader(ByteBuf data) {
        super(data);
    }

    public int getVersion() {
        return content().getByte(3);
    }

    public int getFlags() {
        return content().getByte(4);
    }

    public boolean isHasAudio() {
        return (content().getByte(4) & 0x04) > 0;
    }

    public boolean isHasVideo() {
        return (content().getByte(4) & 0x01) > 0;
    }
}
