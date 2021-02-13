package Hilligans.Tag;

import java.nio.ByteBuffer;

public class FloatTag extends Tag {

    float val;

    public FloatTag() {}

    public FloatTag(float val) {
        this.val = val;
    }

    @Override
    int getSize() {
        return 4;
    }

    @Override
    byte getId() {
        return 4;
    }

    @Override
    public void read(ByteBuffer byteBuf) {
        byteBuf.putFloat(val);
    }

    @Override
    public void write(ByteBuffer byteBuf) {
        val = byteBuf.getFloat();
    }
}
