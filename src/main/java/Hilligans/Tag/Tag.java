package Hilligans.Tag;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public abstract class Tag {

    public static ArrayList<TagFetcher> tags = new ArrayList<>();



    abstract int getSize();
    abstract byte getId();
    public abstract void read(ByteBuffer byteBuf);
    public abstract void write(ByteBuffer byteBuf);

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
    }

    public interface TagFetcher {
        Tag get();
    }

}
