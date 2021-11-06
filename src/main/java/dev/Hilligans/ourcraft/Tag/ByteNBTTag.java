package dev.Hilligans.ourcraft.Tag;

import java.nio.ByteBuffer;

public class ByteNBTTag extends NBTTag {

    public byte val;

    public ByteNBTTag() {}

    public ByteNBTTag(byte val) {
        this.val = val;
    }

    @Override
    int getSize() {
        return 1;
    }

    @Override
    public byte getId() {
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

    @Override
    public NBTTag duplicate() {
        return new ByteNBTTag(val);
    }

    @Override
    public String getVal() {
        return val + "";
    }

    @Override
    public String toString() {
        return "ByteTag{" +
                "val=" + val +
                '}';
    }
}
