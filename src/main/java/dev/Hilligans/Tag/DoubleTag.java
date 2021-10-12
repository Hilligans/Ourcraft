package dev.Hilligans.Tag;

import java.nio.ByteBuffer;

public class DoubleTag extends Tag {

    public double val;

    public DoubleTag() {}

    public DoubleTag(double val) {
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
