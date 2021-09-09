package Hilligans.Tag;

import java.nio.ByteBuffer;

public class StringTag extends Tag {

    public String val;

    public StringTag() {}

    public StringTag(String val) {
        this.val = val;
    }

    @Override
    int getSize() {
        return val.length();
    }

    @Override
    public byte getId() {
        return 8;
    }

    @Override
    public void read(ByteBuffer byteBuf) {
        val = readString(byteBuf);
    }

    @Override
    public void write(ByteBuffer byteBuf) {
        writeString(byteBuf,val);
    }

    @Override
    public String getVal() {
        return val;
    }

    @Override
    public String toString() {
        return "StringTag{" +
                "val='" + val + '\'' +
                '}';
    }
}
