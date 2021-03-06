package Hilligans.Tag;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ListTag<T extends Tag> extends Tag {

    public ArrayList<T> tags = new ArrayList<>();

    @Override
    int getSize() {
        return 0;
    }

    @Override
    public byte getId() {
        return 10;
    }

    @Override
    public void read(ByteBuffer byteBuf) {
        int size = byteBuf.getInt();
        byte type = byteBuf.get();
        for(int x = 0; x < size; x++) {
            Tag tag = Tag.tags.get(type).get();
            tag.read(byteBuf);
            tags.add((T)tag);
        }
    }

    @Override
    public void write(ByteBuffer byteBuf) {
        byteBuf.putInt(tags.size());
        if(tags.size() != 0) {
            byteBuf.put(tags.get(0).getId());
        } else {
            byteBuf.put((byte)0);
        }
        for(T tag : tags) {
            tag.write(byteBuf);
        }
    }

    @Override
    public String toString() {
        return "ListTag{" +
                "tags=" + tags +
                '}';
    }
}
