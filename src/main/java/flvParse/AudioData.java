package flvParse;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;

public class AudioData extends DefaultByteBufHolder {
    public AudioData(ByteBuf data) {
        super(data);
    }
}
