package dev.hilligans.engine.tag;

import dev.hilligans.engine.util.IByteArray;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ByteArrayNBTTag extends NBTTag {

    public byte[] val;

    public ByteArrayNBTTag() {}

    public ByteArrayNBTTag(byte[] val) {
        this.val = val;
    }

    @Override
    int getSize() {
        return val.length;
    }

    @Override
    public byte getId() {
        return 7;
    }

    @Override
    public void read(ByteBuffer byteBuf) {
        int length = byteBuf.getInt();
        val = new byte[length];
        for(int x = 0; x < length; x++) {
            val[x] = byteBuf.get();
        }
    }

    @Override
    public void write(ByteBuffer byteBuf) {
        byteBuf.putInt(val.length);
        for (byte b : val) {
            byteBuf.put(b);
        }
    }

    @Override
    public void read(IByteArray byteArray) {
        int length = byteArray.readInt();
        val = new byte[length];
        for(int x = 0; x < length; x++) {
            val[x] = byteArray.readByte();
        }
    }

    @Override
    public void write(IByteArray byteArray) {
        byteArray.writeInt(val.length);
        for (byte b : val) {
            byteArray.writeByte(b);
        }
    }

    @Override
    public NBTTag duplicate() {
        byte[] newBytes = new byte[val.length];
        System.arraycopy(val,0,newBytes,0,newBytes.length);
        return new ByteArrayNBTTag(newBytes);
    }

    @Override
    public String toString() {
        return "ByteArrayTag{" +
                "val=" + Arrays.toString(val) +
                '}';
    }
}
