package Hilligans.Tag;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public abstract class Tag {

    public static ArrayList<TagFetcher> tags = new ArrayList<>();



    abstract int getSize();
    public abstract byte getId();
    public abstract void read(ByteBuffer byteBuf);
    public abstract void write(ByteBuffer byteBuf);

    public String readString(ByteBuffer byteBuffer) {
        byte length = byteBuffer.get();
        StringBuilder string = new StringBuilder();
        for(int x = 0; x < length; x++) {
            string.append(readChar(byteBuffer));
        }
        return string.toString();
    }

    public void writeString(ByteBuffer byteBuffer, String string) {
        byteBuffer.put((byte) string.length());
        for(int x = 0; x < string.length(); x++) {
            byteBuffer.put((byte) string.charAt(x));
        }
    }

    public char readChar(ByteBuffer byteBuffer) {
        return (char) (byteBuffer.get() & 0xFF);
    }

    public String getVal() {
        return "";
    }

    public static void register() {
        tags.add(CompoundTag::new);
        tags.add(ByteTag::new);
        tags.add(ShortTag::new);
        tags.add(IntegerTag::new);
        tags.add(FloatTag::new);
        tags.add(LongTag::new);
        tags.add(DoubleTag::new);
        tags.add(ByteArrayTag::new);
        tags.add(ShortArrayTag::new);
        tags.add(IntegerArrayTag::new);
        tags.add(ListTag::new);
        tags.add(StringTag::new);
    }

    public interface TagFetcher {
        Tag get();
    }

}
