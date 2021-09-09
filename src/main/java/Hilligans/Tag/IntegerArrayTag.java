package Hilligans.Tag;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class IntegerArrayTag extends Tag {

    public int[] val;

    public IntegerArrayTag() {}

    public IntegerArrayTag(int[] val) {
        this.val = val;
    }

    @Override
    int getSize() {
        return val.length * 2;
    }

    @Override
    public byte getId() {
        return 11;
    }

    @Override
    public void read(ByteBuffer byteBuf) {
        int length = byteBuf.getInt();
        val = new int[length];
        for(int x = 0; x < length; x++) {
            val[x] = byteBuf.getInt();
        }
    }

    @Override
    public void write(ByteBuffer byteBuf) {
        byteBuf.putInt(val.length);
        for (int b : val) {
            byteBuf.putInt(b);
        }
    }

    @Override
    public String toString() {
        return "IntegerArrayTag{" +
                "val=" + Arrays.toString(val) +
                '}';
    }
}
