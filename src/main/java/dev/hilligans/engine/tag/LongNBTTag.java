package dev.hilligans.engine.tag;

import dev.hilligans.engine.util.IByteArray;

import java.nio.ByteBuffer;

public class LongNBTTag extends NBTTag {

    public long val;

    public LongNBTTag() {}

    public LongNBTTag(long val) {
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
    public void read(IByteArray byteArray) {
        val = byteArray.readLong();
    }

    @Override
    public void write(IByteArray byteArray) {
        byteArray.writeLong(val);
    }

    @Override
    public NBTTag duplicate() {
        return new LongNBTTag(val);
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
