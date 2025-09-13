package dev.hilligans.engine.tag;

import dev.hilligans.engine.util.IByteArray;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

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
    public void read(IByteArray byteArray) {
        val = new String(byteArray.readBytes(byteArray.readShort()), StandardCharsets.UTF_8);
    }

    @Override
    public void write(IByteArray byteArray) {
        byteArray.writeShort((short) val.length());
        byteArray.writeBytesN(val.getBytes(StandardCharsets.UTF_8));
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
