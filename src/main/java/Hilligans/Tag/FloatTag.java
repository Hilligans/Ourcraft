package Hilligans.Tag;

import java.nio.ByteBuffer;

public class FloatTag extends Tag {

    public float val;

    public FloatTag() {}

    public FloatTag(float val) {
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
