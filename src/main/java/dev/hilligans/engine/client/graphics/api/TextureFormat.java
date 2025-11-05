package dev.hilligans.engine.client.graphics.api;

public enum TextureFormat {
    RGB(3),
    RGBA(4),

    DXT1,
    DXT5,

    OTHER;


    public int channels;

    TextureFormat(int channels) {
        this.channels = channels;
    }

    TextureFormat() {
        this(0);
    }

    public int getChannels() {
        return channels;
    }


    public static TextureFormat from(int channels) {
        if(channels == 4) {
            return RGBA;
        } else if(channels == 3) {
            return RGB;
        }

        return OTHER;
    }
}
