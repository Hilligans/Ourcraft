package dev.Hilligans.ourcraft.Tag;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ShortArrayTag extends Tag {

    public short[] val;

    public ShortArrayTag() {}

    public ShortArrayTag(short[] val) {
        this.val = val;
    }

    @Override
    int getSize() {
        return val.length * 2;
    }

    @Override
    public byte getId() {
        return 13;
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

    @Override
    public String toString() {
        return "ShortArrayTag{" +
                "val=" + Arrays.toString(val) +
                '}';
    }
}
