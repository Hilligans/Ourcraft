package dev.hilligans.engine.tag;

import dev.hilligans.engine.util.IByteArray;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class LongArrayNBTTag extends NBTTag {

    public long[] val;

    public LongArrayNBTTag() {

    }

    public LongArrayNBTTag(long[] val) {
        this.val = val;
    }

    @Override
    int getSize() {
        return 0;
    }

    @Override
    public byte getId() {
        return 12;
    }

    @Override
    public void read(ByteBuffer byteBuf) {
        int length = byteBuf.getInt();
        val = new long[length];
        for(int x = 0; x < length; x++) {
            val[x] = byteBuf.getLong();
        }
    }

    @Override
    public void write(ByteBuffer byteBuf) {
        byteBuf.putInt(val.length);
        for (long b : val) {
            byteBuf.putLong(b);
        }
    }

    @Override
    public void read(IByteArray byteArray) {
        val = byteArray.readLongs(byteArray.readInt());
    }

    @Override
    public void write(IByteArray byteArray) {
        byteArray.writeInt(val.length);
        for (long b : val) {
            byteArray.writeLong(b);
        }
    }

    @Override
    public NBTTag duplicate() {
        long[] newLongs = new long[val.length];
        System.arraycopy(val,0,newLongs,0,newLongs.length);
        return new LongArrayNBTTag(newLongs);
    }

    @Override
    public String toString() {
        return "LongArrayTag{" +
                "val=" + Arrays.toString(val) +
                ",size=" + val.length +
                '}';
    }
}
