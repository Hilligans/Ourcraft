package dev.hilligans.ourcraft.tag;

import java.nio.ByteBuffer;

public class StringNBTTag extends NBTTag {

    public String val;

    public StringNBTTag() {}

    public StringNBTTag(String val) {
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
    public NBTTag duplicate() {
        return new StringNBTTag(val);
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
