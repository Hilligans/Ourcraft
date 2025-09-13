package dev.hilligans.engine.tag;

import dev.hilligans.engine.util.IByteArray;

import java.nio.ByteBuffer;

public class FullStringNBTTag extends NBTTag {

    public String val;

    public FullStringNBTTag() {}

    public FullStringNBTTag(String val) {
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
    public void read(IByteArray byteArray) {

    }

    @Override
    public void write(IByteArray byteArray) {

    }

    @Override
    public NBTTag duplicate() {
        return new FullStringNBTTag(val);
    }

    @Override
    public String getVal() {
        return val;
    }

}
