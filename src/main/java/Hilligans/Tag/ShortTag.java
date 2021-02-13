package Hilligans.Tag;

import java.nio.ByteBuffer;

public class ShortTag extends Tag {

    public short val;

    public ShortTag() {}

    public ShortTag(short val) {
        this.val = val;
    }

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
