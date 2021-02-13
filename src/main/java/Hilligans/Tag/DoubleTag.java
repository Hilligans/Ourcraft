package Hilligans.Tag;

import java.nio.ByteBuffer;

public class DoubleTag extends Tag {

    double val;

    public DoubleTag() {}

    public DoubleTag(double val) {
        this.val = val;
    }

    @Override
    int getSize() {
        return 8;
    }

    @Override
    byte getId() {
        return 6;
    }

    @Override
    public void read(ByteBuffer byteBuf) {
        val = byteBuf.getDouble();
    }

    @Override
    public void write(ByteBuffer byteBuf) {
        byteBuf.putDouble(val);
    }
}
