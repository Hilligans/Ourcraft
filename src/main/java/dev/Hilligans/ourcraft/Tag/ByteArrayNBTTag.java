package dev.Hilligans.ourcraft.Tag;

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
    public String toString() {
        return "ByteArrayTag{" +
                "val=" + Arrays.toString(val) +
                '}';
    }
}
