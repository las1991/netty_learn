package flvParse;

import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AudioTagHeader {

    private static final Logger LOGGER = LoggerFactory.getLogger(AudioTagHeader.class);

    enum SoundFormat {
        //Linear PCM,platform endian
        LINEAR_PCM_ENDIAN,
        ADPCM,
        MP3,
        //Linear PCM,little endian
        LINEAR_PCM_LITTLE,
        NELLYMOSER_16KHZ_MONO,
        NELLYMOSER_8KHZ_MONO,
        GETNELLYMOSER_16KHZ_MONO,
        //G.711 A-law logarithmic PCM
        G711A,
        //G.711 mu-law logarithmic PCM
        G711U,
        RESERVED,
        AAC,
        SPEEX,
        MP3_8KHZ,
        DEVICE_SPECIFIC;

        public static SoundFormat valueOf(int format) {
            for (SoundFormat soundFormat : SoundFormat.values()) {
                if (soundFormat.ordinal() == format) {
                    return soundFormat;
                }
            }
            return null;
        }
    }

    enum SoundRate {
        $5_5_000(5500), $11_000(11000), $22_000(22000), $44_000(44000);
        private int val;

        SoundRate(int val) {
            this.val = val;
        }

        public int getVal() {
            return val;
        }

        public static SoundRate valueOf(int rate) {
            for (SoundRate soundRate : SoundRate.values()) {
                if (soundRate.val == rate) {
                    return soundRate;
                }
            }
            return null;
        }
    }


    private SoundFormat soundFormat;
    private SoundRate soundRate;
    private int soundSize;
    private int soundType;

    private int aacPacketType;

    public AudioTagHeader(ByteBuf content) {
        try {
            if (content.readableBytes() < 1) {
                throw new IllegalArgumentException("content is null");
            }
            int format = content.getByte(0) & 0xf0;
            if (format < 0 || format > SoundFormat.values().length) {
                LOGGER.warn("format {} is unsupport !", format);
            } else {
                this.soundFormat = SoundFormat.valueOf(format);
            }

            int rate = content.getByte(0) & 0x0c;
            if (rate < 0 || rate > SoundRate.values().length) {
                LOGGER.warn("rate {} is unsupport !", rate);
            } else {
                this.soundRate = SoundRate.valueOf(rate);
            }

            this.soundSize = content.getByte(0) & 0x02;
            this.soundType = content.getByte(0) & 0x01;

        } finally {
            content.release();
        }
    }

    public SoundFormat getSoundFormat() {
        return soundFormat;
    }

    public void setSoundFormat(SoundFormat soundFormat) {
        this.soundFormat = soundFormat;
    }

    public SoundRate getSoundRate() {
        return soundRate;
    }

    public void setSoundRate(SoundRate soundRate) {
        this.soundRate = soundRate;
    }

    public int getSoundSize() {
        return soundSize;
    }

    public void setSoundSize(int soundSize) {
        this.soundSize = soundSize;
    }

    public int getSoundType() {
        return soundType;
    }

    public void setSoundType(int soundType) {
        this.soundType = soundType;
    }

    @Override
    public String toString() {
        return "AudioTagHeader{" +
                "soundFormat=" + soundFormat +
                ", soundRate=" + soundRate +
                ", soundSize=" + soundSize +
                ", soundType=" + soundType +
                '}';
    }
}
