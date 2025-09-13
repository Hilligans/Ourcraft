package dev.hilligans.engine.tag;

import dev.hilligans.engine.util.IByteArray;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public abstract class NBTTag {

    public static ArrayList<TagFetcher> tags = new ArrayList<>();

    abstract int getSize();
    public abstract byte getId();
    public abstract void read(ByteBuffer byteBuf);
    public abstract void write(ByteBuffer byteBuf);
    public abstract void read(IByteArray byteArray);
    public abstract void write(IByteArray byteArray);
    public abstract NBTTag duplicate();

    public static String readString(ByteBuffer byteBuffer) {

        short size = byteBuffer.getShort();
        byte[] vals = new byte[size];
        for(int x = 0; x < size; x++) {
            vals[x] = byteBuffer.get();
        }
        return new String(vals,StandardCharsets.UTF_8);
    }

    public String readFullString(ByteBuffer byteBuffer) {
        byte length = byteBuffer.get();
        StringBuilder string = new StringBuilder();
        for(int x = 0; x < length; x++) {
            string.append(readFullChar(byteBuffer));
        }
        return string.toString();
    }

    public static void writeString(ByteBuffer byteBuffer, String string) {

        byte[] vals = string.getBytes(StandardCharsets.UTF_8);
        byteBuffer.putShort((short) vals.length);
        for(byte val : vals) {
            byteBuffer.put(val);
        }
    }

    public void writeFullString(ByteBuffer byteBuffer, String string) {
        byteBuffer.put((byte) string.length());
        for(int x = 0; x < string.length(); x++) {
            byteBuffer.putShort((short) string.charAt(x));
        }
    }

    public char readChar(ByteBuffer byteBuffer) {
        return (char) (byteBuffer.get() & 0xFF);
    }

    public char readFullChar(ByteBuffer byteBuffer) {
        return (char) byteBuffer.getShort();
    }

    public String getVal() {
        return "";
    }


    static {
        tags.add(EndNBTTag::new);
        tags.add(ByteNBTTag::new);
        tags.add(ShortNBTTag::new);
        tags.add(IntegerNBTTag::new);
        tags.add(LongNBTTag::new);
        tags.add(FloatNBTTag::new);
        tags.add(DoubleNBTTag::new);
        tags.add(ByteArrayNBTTag::new);
        tags.add(StringNBTTag::new);
        tags.add(ListNBTTag::new);
        tags.add(CompoundNBTTag::new);
        tags.add(IntegerArrayNBTTag::new);
        tags.add(LongArrayNBTTag::new);
    }

    public interface TagFetcher {
        NBTTag get();
    }

}
