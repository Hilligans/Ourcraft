package Hilligans.Tag;

import java.nio.ByteBuffer;

public class LongTag extends Tag {

    public long val;

    public LongTag() {}

    public LongTag(long val) {
        this.val = val;
    }

    @Override
    int getSize() {
        return 8;
    }

    @Override
    public byte getId() {
        return 5;
    }

    @Override
    public void read(ByteBuffer byteBuf) {
        val = byteBuf.getLong();
    }

    @Override
    public void write(ByteBuffer byteBuf) {
        byteBuf.putLong(val);
    }

    @Override
    public String getVal() {
        return val + "";
    }
}
