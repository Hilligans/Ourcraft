package dev.hilligans.ourcraft.tag;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ListNBTTag<T extends NBTTag> extends NBTTag {

    public ArrayList<T> tags = new ArrayList<>();

    @Override
    int getSize() {
        return 0;
    }

    @Override
    public byte getId() {
        return 9;
    }

    @Override
    public void read(ByteBuffer byteBuf) {
        byte type = byteBuf.get();
        int size = byteBuf.getInt();
        for(int x = 0; x < size; x++) {
            NBTTag nbtTag = NBTTag.tags.get(type).get();
            nbtTag.read(byteBuf);
            tags.add((T) nbtTag);
        }
    }

    @Override
    public void write(ByteBuffer byteBuf) {
        if(tags.size() != 0) {
            byteBuf.put(tags.get(0).getId());
        } else {
            byteBuf.put((byte)0);
        }
        byteBuf.putInt(tags.size());
        for(T tag : tags) {
            tag.write(byteBuf);
        }
    }

    @Override
    public NBTTag duplicate() {
        ListNBTTag<T> listNBTTag = new ListNBTTag<>();
        listNBTTag.tags.addAll(tags);
        return listNBTTag;
    }

    @Override
    public String toString() {
        return "ListTag{" +
                "tags=" + tags +
                '}';
    }
}
