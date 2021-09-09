package Hilligans.Tag;

import java.io.DataOutput;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public abstract class Tag {

    public static ArrayList<TagFetcher> tags = new ArrayList<>();



    abstract int getSize();
    public abstract byte getId();
    public abstract void read(ByteBuffer byteBuf);
    public abstract void write(ByteBuffer byteBuf);

    public static String readString(ByteBuffer byteBuffer) {

        short size = byteBuffer.getShort();
        byte[] vals = new byte[size];
        for(int x = 0; x < size; x++) {
            vals[x] = byteBuffer.get();
        }
        return new String(vals,StandardCharsets.UTF_8);

        //byte length = byteBuffer.get();
       // StringBuilder string = new StringBuilder();
        //for(int x = 0; x < length; x++) {
        //    string.append(readChar(byteBuffer));
        //}
        //return string.toString();
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
        //byteBuffer.put((byte) string.length());
       // for(int x = 0; x < string.length(); x++) {
       //     byteBuffer.put((byte) string.charAt(x));
       // }
       // //DataOutput a;
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

    public static void register() {
        tags.add(CompoundTag::new);
        tags.add(ByteTag::new);
        tags.add(ShortTag::new);
        tags.add(IntegerTag::new);
        tags.add(LongTag::new);
        tags.add(FloatTag::new);
        tags.add(DoubleTag::new);
        tags.add(ByteArrayTag::new);
        tags.add(StringTag::new);
        tags.add(ListTag::new);
        tags.add(CompoundTag::new);
        tags.add(IntegerArrayTag::new);
        tags.add(LongArrayTag::new);
    }

    public interface TagFetcher {
        Tag get();
    }

}
