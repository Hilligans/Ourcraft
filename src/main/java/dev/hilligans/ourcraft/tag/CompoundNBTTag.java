package dev.hilligans.ourcraft.tag;

import dev.hilligans.ourcraft.Ourcraft;
import dev.hilligans.ourcraft.item.ItemStack;
import dev.hilligans.ourcraft.util.IByteArray;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;

public class CompoundNBTTag extends NBTTag {

    public HashMap<String, NBTTag> tags = new HashMap<>();

    @Override
    int getSize() {
        return 0;
    }

    public CompoundNBTTag() {}


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

    public ByteNBTTag getByteTag(String name) {
        return (ByteNBTTag) getTag(name);
    }

    public ShortNBTTag getShortTag(String name) {
        return (ShortNBTTag) getTag(name);
    }

    public IntegerNBTTag getIntTag(String name) {
        return (IntegerNBTTag)getTag(name);
    }

    public StringNBTTag getStringTag(String id) {
        return (StringNBTTag)getTag(id);
    }

    public DoubleNBTTag getDoubleTag(String id) {
        return (DoubleNBTTag)getTag(id);
    }

    public FloatNBTTag getFloatTag(String id) {
        return (FloatNBTTag)getTag(id);
    }

    public LongNBTTag getLongTag(String id) {
        return (LongNBTTag)getTag(id);
    }

    public boolean getBooleanTag(String id) {
        return ((ByteNBTTag)getTag(id)).val == 1;
    }

    public FullStringNBTTag getFullStringTag(String id) {
        return (FullStringNBTTag)getTag(id);
    }


    public byte getByte(String name) {
        return getByteTag(name).val;
    }

    public short getShort(String name) {
        return getShortTag(name).val;
    }

    public int getInt(String name) {
        return ((IntegerNBTTag)getTag(name)).val;
    }

    public long getLong(String name) {
        return getLongTag(name).val;
    }

    public String getString(String name) {
        return getStringTag(name).val;
    }

    public float getFloat(String name) {
        return getFloatTag(name).val;
    }

    public double getDouble(String name) {
        return getDoubleTag(name).val;
    }

    public boolean getBoolean(String name) {
        return getBooleanTag(name);
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
    public void read(IByteArray byteArray) {
        byte tagId;
        while((tagId = byteArray.readByte()) != 0) {
            String string = new String(byteArray.readBytes(byteArray.readShort()), StandardCharsets.UTF_8);
            NBTTag nbtTag = NBTTag.tags.get(tagId).get();
            nbtTag.read(byteArray);
            tags.put(string, nbtTag);
        }
    }

    @Override
    public void write(IByteArray byteArray) {
        Collection<String> tagCollection = tags.keySet();
        for(String string : tagCollection) {
            byteArray.writeByte(tags.get(string).getId());
            byteArray.writeShort((short) string.length());
            byteArray.writeBytesN(string.getBytes(StandardCharsets.UTF_8));
            tags.get(string).write(byteArray);
        }
        byteArray.writeByte((byte)0);
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
