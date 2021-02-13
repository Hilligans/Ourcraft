package Hilligans.Tag;

import Hilligans.Item.ItemStack;
import Hilligans.Item.Items;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class CompoundTag extends Tag {

    HashMap<String, Tag> tags = new HashMap<>();

    @Override
    int getSize() {
        return 0;
    }


    public CompoundTag putTag(String id, Tag tag) {
        tags.put(id,tag);
        return this;
    }

    public CompoundTag putByte(String id, byte val) {
        putTag(id,new ByteTag(val));
        return this;
    }

    public CompoundTag putShort(String id, short val) {
        putTag(id,new ShortTag(val));
        return this;
    }

    public CompoundTag putInt(String id, int val) {
        putTag(id,new IntegerTag(val));
        return this;
    }

    public Tag getTag(String name) {
        return tags.get(name);
    }

    public ItemStack readStack(int slot) {
        CompoundTag compoundTag = (CompoundTag) getTag("slot" + slot);
        byte count = ((ByteTag)compoundTag.getTag("count")).val;
        short item = ((ShortTag)compoundTag.getTag("item")).val;
        if(item == -1) {
            return ItemStack.emptyStack();
        }
        return new ItemStack(Items.ITEMS.get(item),count);
    }

    public CompoundTag writeStack(int slot, ItemStack itemStack) {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putByte("count",itemStack.count);
        if(itemStack.isEmpty()) {
            compoundTag.putShort("item",(short)-1);
        } else {
            compoundTag.putShort("item", (short) itemStack.item.id);
        }
        putTag("slot" + slot, compoundTag);
        return this;
    }

    @Override
    byte getId() {
        return 0;
    }

    @Override
    public void read(ByteBuffer byteBuf) {
        byte tagCount = byteBuf.get();
        for(int x = 0; x < tagCount; x++) {
            String string = readString(byteBuf);
            byte tagId = byteBuf.get();
            Tag tag = Tag.tags.get(tagId).get();
            tag.read(byteBuf);
            tags.put(string,tag);
        }
    }

    @Override
    public void write(ByteBuffer byteBuf) {
        Collection<String> tagCollection = tags.keySet();
        byte length = (byte) tagCollection.size();
        byteBuf.put(length);
        for(String string : tagCollection) {
            writeString(byteBuf,string);
            byteBuf.put(tags.get(string).getId());
            tags.get(string).write(byteBuf);
        }
    }

    private String readString(ByteBuffer byteBuffer) {
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

    @Override
    public String toString() {
        return "CompoundTag{" +
                "tags=" + tags +
                '}';
    }
}
