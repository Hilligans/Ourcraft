package Hilligans.Tag;

import java.nio.ByteBuffer;

public class ByteTag extends Tag {

    byte val;

    public ByteTag() {}

    public ByteTag(byte val) {
        this.val = val;
    }

    @Override
    int getSize() {
        return 1;
    }

    @Override
    byte getId() {
        return 1;
    }

    @Override
    public void read(ByteBuffer byteBuf) {
        val = byteBuf.get();
    }

    @Override
    public void write(ByteBuffer byteBuf) {
        byteBuf.put(val);
    }
}
