package Hilligans.Network;

import Hilligans.Data.Other.ColoredString;
import Hilligans.Item.Item;
import Hilligans.Item.ItemStack;
import Hilligans.Item.Items;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.joml.Vector4f;

public class PacketData {


    public ChannelHandlerContext ctx;

    ByteBuf byteBuf;
    int packetId = 0;
    public int size = 0;

    public PacketData(PacketBase packetBase) {
        byteBuf = Unpooled.buffer();
        packetId = packetBase.packetId;
        packetBase.encode(this);
    }

    public PacketData(int val) {
        byteBuf = Unpooled.buffer();
        byteBuf.writeByte(val);
    }

    public PacketData(byte[] bytes) {
        byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(bytes);
        packetId = byteBuf.readInt();
    }

    public PacketData(ByteBuf byteBuf) {
        packetId = byteBuf.readInt();
        byteBuf.readBytes(packetId);
    }

    public void writeToByteBuf(ByteBuf byteBuf) {
        byteBuf.writeInt(size + 8);
        byteBuf.writeInt(packetId);
        byteBuf.writeBytes(this.byteBuf);
    }

    public int readInt() {
        size -= 4;
        return byteBuf.readInt();
    }

    public float readFloat() {
        size -= 4;
        return byteBuf.readFloat();
    }

    public short readShort() {
        size -= 2;
        return byteBuf.readShort();
    }

    public byte readByte() {
        size -= 1;
        return byteBuf.readByte();
    }

    public String readString() {
        short stringLength = readShort();
        if(stringLength == -1) {
            return " ";
        }
        StringBuilder val = new StringBuilder();
        for(short x = 0; x < stringLength; x++) {
            char val1 = (char) (readByte() & 0xFF);
            val.append(val1);
        }
        return val.toString();
    }

    public boolean readBoolean() {
        byte val = readByte();
        return val == (byte) 1;
    }

    public ColoredString readColoredString() {
        short stringLength = readShort();
        ColoredString coloredString = new ColoredString("");
        for(short x = 0; x < stringLength; x++) {
            char val1 = (char) (readByte() & 0xFF);
            Vector4f vec4 = new Vector4f(readFloat(),readFloat(),readFloat(),readFloat());
            coloredString.append(val1,vec4);
        }
        return coloredString;
    }

    public ItemStack readItemStack() {
        short item = readShort();
        byte count = readByte();
        if(item != -1) {
            return new ItemStack(Items.ITEMS.get(item), count);
        } else {
            return new ItemStack(null,count);
        }
    }

    public void writeInt(int val) {
        size += 4;
        byteBuf.writeInt(val);
    }

    public void writeFloat(float val) {
        size += 4;
        byteBuf.writeFloat(val);
    }

    public void writeShort(short val) {
        size += 2;
        byteBuf.writeShort(val);
    }

    public void writeByte(byte val) {
        size += 1;
        byteBuf.writeByte(val);
    }

    public void writeString(String val) {
        if(val.length() == 0) {
            writeShort((short)-1);
            return;
        }
        short stringLength = (short) val.length();
        writeShort(stringLength);

        //writeShort(stringLength);
        for(short x = 0; x < stringLength; x++) {
            writeByte((byte)val.charAt(x));
        }
    }

    public void writeBoolean(boolean val) {
        if(val) {
            writeByte((byte)1);
        } else {
            writeByte((byte)0);
        }
    }

    public void writeColoredString(ColoredString coloredString) {
        short stringLength = (short) coloredString.string.length();
        writeShort(stringLength);

        //writeShort(stringLength);
        for(short x = 0; x < stringLength; x++) {
            writeByte((byte)coloredString.string.charAt(x));
            Vector4f vector4f = coloredString.colors.get(x);
            writeFloat(vector4f.x);
            writeFloat(vector4f.y);
            writeFloat(vector4f.z);
            writeFloat(vector4f.w);
        }
    }

    public void writeItemStack(ItemStack itemStack) {
        Item item = itemStack.item;
        if(item != null) {
            writeShort((short) itemStack.item.id);
        } else {
            writeShort((short)-1);
        }
        writeByte(itemStack.count);
    }

    public PacketBase createPacket() {
        PacketBase packetBase = PacketBase.packets.get(packetId).getPacket();
        packetBase.ctx = ctx;
        packetBase.decode(this);
        return packetBase;
    }




}
