package dev.hilligans.ourcraft.tag;

import dev.hilligans.ourcraft.Ourcraft;
import dev.hilligans.ourcraft.item.ItemStack;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;

public class CompoundNBTTag extends NBTTag {

    public HashMap<String, NBTTag> tags = new HashMap<>();

    @Override
    int getSize() {
        return 0;
    }


    public CompoundNBTTag putTag(String id, NBTTag NBTTag) {
        tags.put(id, NBTTag);
        if(NBTTag == this) {
            System.err.println("Attempting to put compound tag inside the same compound tag, this will cause an error when saving");
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public CompoundNBTTag putByte(String id, byte val) {
        putTag(id,new ByteNBTTag(val));
        return this;
    }

    public CompoundNBTTag putShort(String id, short val) {
        putTag(id,new ShortNBTTag(val));
        return this;
    }

    public CompoundNBTTag putInt(String id, int val) {
        putTag(id,new IntegerNBTTag(val));
        return this;
    }

    public CompoundNBTTag putString(String id, String string) {
        putTag(id,new StringNBTTag(string));
        return this;
    }

    public CompoundNBTTag putFullString(String id, String string) {
        putTag(id,new FullStringNBTTag(string));
        return this;
    }

    public CompoundNBTTag putDouble(String id, double val) {
        putTag(id, new DoubleNBTTag(val));
        return this;
    }

    public CompoundNBTTag putFloat(String id, float val) {
        putTag(id, new FloatNBTTag(val));
        return this;
    }

    public CompoundNBTTag putLong(String id, long val) {
        putTag(id, new LongNBTTag(val));
        return this;
    }

    public CompoundNBTTag putBoolean(String id, boolean val) {
        putTag(id, new ByteNBTTag((byte) (val ? 1 : 0)));
        return this;
    }

    public NBTTag getTag(String name) {
        return tags.get(name);
    }

    public IntegerNBTTag getIntTag(String name) {
        return (IntegerNBTTag)getTag(name);
    }

    public StringNBTTag getString(String id) {
        return (StringNBTTag)getTag(id);
    }

    public DoubleNBTTag getDouble(String id) {
        return (DoubleNBTTag)getTag(id);
    }

    public FloatNBTTag getFloat(String id) {
        return (FloatNBTTag)getTag(id);
    }

    public LongNBTTag getLong(String id) {
        return (LongNBTTag)getTag(id);
    }

    public boolean getBoolean(String id) {
        return ((ByteNBTTag)getTag(id)).val == 1;
    }

    public FullStringNBTTag getFullString(String id) {
        return (FullStringNBTTag)getTag(id);
    }

    public int getInt(String name) {
        return ((IntegerNBTTag)getTag(name)).val;
    }

    public CompoundNBTTag getCompoundTag(String name) {
        return (CompoundNBTTag) tags.get(name);
    }

    public ItemStack readStack(int slot) {
        CompoundNBTTag compoundTag = (CompoundNBTTag) getTag("slot" + slot);
        int count = ((IntegerNBTTag)compoundTag.getTag("count")).val;
        short item = ((ShortNBTTag)compoundTag.getTag("item")).val;
        if(item == -1) {
            return ItemStack.emptyStack();
        }
        return new ItemStack(Ourcraft.GAME_INSTANCE.getItem(item),count);
    }

    public CompoundNBTTag writeStack(int slot, ItemStack itemStack) {
        CompoundNBTTag compoundTag = new CompoundNBTTag();
        compoundTag.putInt("count",itemStack.count);
        if(itemStack.isEmpty()) {
            compoundTag.putShort("item",(short)-1);
        } else {
            compoundTag.putShort("item", (short) itemStack.item.id);
        }
        putTag("slot" + slot, compoundTag);
        return this;
    }

    public CompoundNBTTag getFirstCompoundTag() {
        for(String s : tags.keySet()) {
            NBTTag tag = tags.get(s);
            if(tag instanceof CompoundNBTTag) {
                return (CompoundNBTTag) tag;
            }
        }
        return null;
    }

    @Override
    public byte getId() {
        return 10;
    }

    @Override
    public void read(ByteBuffer byteBuf) {
        byte tagId;
        while((tagId = byteBuf.get()) != 0) {
            String string = readString(byteBuf);
            NBTTag nbtTag = NBTTag.tags.get(tagId).get();
            nbtTag.read(byteBuf);
            tags.put(string, nbtTag);
        }
    }

    @Override
    public void write(ByteBuffer byteBuf) {
        Collection<String> tagCollection = tags.keySet();
        for(String string : tagCollection) {
            byteBuf.put(tags.get(string).getId());
            writeString(byteBuf,string);
            tags.get(string).write(byteBuf);
        }
        byteBuf.put((byte)0);
    }

    @Override
    public NBTTag duplicate() {
        CompoundNBTTag compoundNBTTag = new CompoundNBTTag();
        for(String tag : tags.keySet()) {
            compoundNBTTag.putTag(tag,tags.get(tag));
        }
        return compoundNBTTag;
    }

    public void writeTo(ByteBuffer buffer) {
        buffer.put(getId());
        writeString(buffer,"");
        write(buffer);
    }

    public void readFrom(ByteBuffer buffer) {
        buffer.get();
        readString(buffer);
        read(buffer);
    }

    @Override
    public String toString() {
        return "CompoundTag{" +
                "tags=" + tags +
                '}';
    }
}
