package Hilligans.Tag;

import java.nio.ByteBuffer;

public class FullStringTag extends Tag {

    public String val;

    public FullStringTag() {}

    public FullStringTag(String val) {
        this.val = val;
    }

    @Override
    int getSize() {
        return val.length();
    }

    @Override
    public byte getId() {
        return 12;
    }

    @Override
    public void read(ByteBuffer byteBuf) {
        val = readFullString(byteBuf);
    }

    @Override
    public void write(ByteBuffer byteBuf) {
        writeFullString(byteBuf,val);
    }

    @Override
    public String getVal() {
        return val;
    }

}
