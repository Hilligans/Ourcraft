package Hilligans.Tag;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class LongArrayTag extends Tag {

    public long[] val;

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
    public String toString() {
        return "LongArrayTag{" +
                "val=" + Arrays.toString(val) +
                ",size=" + val.length +
                '}';
    }
}
