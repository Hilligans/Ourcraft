package dev.Hilligans.ourcraft.Tag;

import java.nio.ByteBuffer;

public class FloatNBTTag extends NBTTag {

    public float val;

    public FloatNBTTag() {}

    public FloatNBTTag(float val) {
        this.val = val;
    }

    @Override
    int getSize() {
        return 4;
    }

    @Override
    public byte getId() {
        return 5;
    }

    @Override
    public void read(ByteBuffer byteBuf) {
        byteBuf.putFloat(val);
    }

    @Override
    public void write(ByteBuffer byteBuf) {
        val = byteBuf.getFloat();
    }

    @Override
    public NBTTag duplicate() {
        return new FloatNBTTag(val);
    }

    @Override
    public String getVal() {
        return val + "";
    }

    @Override
    public String toString() {
        return "FloatTag{" +
                "val=" + val +
                '}';
    }
}
