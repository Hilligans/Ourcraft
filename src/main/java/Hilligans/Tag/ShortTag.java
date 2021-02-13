package Hilligans.Tag;

import java.nio.ByteBuffer;

public class ShortTag extends Tag {

    short val;

    @Override
    int getSize() {
        return 2;
    }

    @Override
    byte getId() {
        return 2;
    }

    @Override
    public void read(ByteBuffer byteBuf) {
        val = byteBuf.getShort();
    }

    @Override
    public void write(ByteBuffer byteBuf) {
        byteBuf.putShort(val);
    }
}
