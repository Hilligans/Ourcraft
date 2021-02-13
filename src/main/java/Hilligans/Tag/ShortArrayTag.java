package Hilligans.Tag;

import java.nio.ByteBuffer;

public class ShortArrayTag extends Tag {

    short[] val;

    public ShortArrayTag() {}

    public ShortArrayTag(short[] val) {
        this.val = val;
    }

    @Override
    int getSize() {
        return val.length * 2;
    }

    @Override
    byte getId() {
        return 8;
    }

    @Override
    public void read(ByteBuffer byteBuf) {
        int length = byteBuf.getInt();
        val = new short[length];
        for(int x = 0; x < length; x++) {
            val[x] = byteBuf.getShort();
        }
    }

    @Override
    public void write(ByteBuffer byteBuf) {
        byteBuf.putInt(val.length);
        for (short b : val) {
            byteBuf.putShort(b);
        }
    }
}
