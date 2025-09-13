package dev.hilligans.engine.tag;

import dev.hilligans.engine.util.IByteArray;

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
    public void read(IByteArray byteArray) {
        val = byteArray.readByte();
    }

    @Override
    public void write(IByteArray byteArray) {
        byteArray.writeByte(val);
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
