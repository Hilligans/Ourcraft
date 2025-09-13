package dev.hilligans.engine.tag;

import dev.hilligans.engine.util.IByteArray;

import java.nio.ByteBuffer;

public class DoubleNBTTag extends NBTTag {

    public double val;

    public DoubleNBTTag() {}

    public DoubleNBTTag(double val) {
        this.val = val;
    }

    @Override
    int getSize() {
        return 8;
    }

    @Override
    public byte getId() {
        return 6;
    }

    @Override
    public void read(ByteBuffer byteBuf) {
        val = byteBuf.getDouble();
    }

    @Override
    public void write(ByteBuffer byteBuf) {
        byteBuf.putDouble(val);
    }

    @Override
    public void read(IByteArray byteArray) {
        val = byteArray.readDouble();
    }

    @Override
    public void write(IByteArray byteArray) {
        byteArray.writeDouble(val);
    }

    @Override
    public NBTTag duplicate() {
        return new DoubleNBTTag(val);
    }

    @Override
    public String getVal() {
        return val + "";
    }

    @Override
    public String toString() {
        return "DoubleTag{" +
                "val=" + val +
                '}';
    }
}
