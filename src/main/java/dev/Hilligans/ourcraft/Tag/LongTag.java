package dev.Hilligans.ourcraft.Tag;

import java.nio.ByteBuffer;

public class LongTag extends Tag {

    public long val;

    public LongTag() {}

    public LongTag(long val) {
        this.val = val;
    }

    @Override
    int getSize() {
        return 8;
    }

    @Override
    public byte getId() {
        return 4;
    }

    @Override
    public void read(ByteBuffer byteBuf) {
        val = byteBuf.getLong();
    }

    @Override
    public void write(ByteBuffer byteBuf) {
        byteBuf.putLong(val);
    }

    @Override
    public String getVal() {
        return val + "";
    }

    @Override
    public String toString() {
        return "LongTag{" +
                "val=" + val +
                '}';
    }
}
