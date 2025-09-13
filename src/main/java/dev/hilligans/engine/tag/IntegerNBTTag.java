package dev.hilligans.engine.tag;

import dev.hilligans.engine.util.IByteArray;

import java.nio.ByteBuffer;

public class IntegerNBTTag extends NBTTag {

    public int val;

    public IntegerNBTTag() {}

    public IntegerNBTTag(int val) {
        this.val = val;
    }


    @Override
    int getSize() {
        return 4;
    }

    @Override
    public byte getId() {
        return 3;
    }

    @Override
    public void read(ByteBuffer byteBuf) {
        val = byteBuf.getInt();
    }

    @Override
    public void write(ByteBuffer byteBuf) {
        byteBuf.putInt(val);
    }

    @Override
    public void read(IByteArray byteArray) {
        val = byteArray.readInt();
    }

    @Override
    public void write(IByteArray byteArray) {
        byteArray.writeInt(val);
    }

    @Override
    public NBTTag duplicate() {
        return new IntegerNBTTag(val);
    }

    @Override
    public String getVal() {
        return val + "";
    }

    @Override
    public String toString() {
        return "IntegerTag{" +
                "val=" + val +
                '}';
    }
}
