package dev.hilligans.engine.tag;

import dev.hilligans.engine.util.IByteArray;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ShortArrayNBTTag extends NBTTag {

    public short[] val;

    public ShortArrayNBTTag() {}

    public ShortArrayNBTTag(short[] val) {
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
    public void read(IByteArray byteArray) {
        val = byteArray.readShorts(byteArray.readInt());
    }

    @Override
    public void write(IByteArray byteArray) {
        byteArray.writeInt(val.length);
        for (short b : val) {
            byteArray.writeShort(b);
        }
    }

    @Override
    public NBTTag duplicate() {
        return null;
    }

    @Override
    public String toString() {
        return "ShortArrayTag{" +
                "val=" + Arrays.toString(val) +
                '}';
    }
}
