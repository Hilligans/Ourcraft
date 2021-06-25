package Hilligans.Util;

import Hilligans.Data.Other.ColoredString;
import Hilligans.Item.Item;
import Hilligans.Item.ItemStack;
import Hilligans.Item.Items;
import Hilligans.Network.PacketBase;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.joml.Vector4f;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ByteArray {


    public ByteBuf byteBuf;
    public int size = 0;

    public ByteArray() {
    }

    public ByteArray(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
        this.size = byteBuf.readableBytes();
    }

    public ByteArray(byte[] bytes) {
        byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(bytes);
        this.size = bytes.length;
    }

    public ByteArray(ByteBuffer buffer) {
        this(buffer.array());
    }

    public ByteBuffer toByteBuffer() {
        byte[] bytes = byteBuf.array();
        ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length);
        buffer.mark();
        buffer.put(bytes);
        buffer.reset();
        return buffer;
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

    public long readLong() {
        size -= 8;
        return byteBuf.readLong();
    }

    public double readDouble() {
        size -= 8;
        return byteBuf.readDouble();
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
        int count = readInt();
        if(item != -1) {
            return new ItemStack(Items.getItem(item), count);
        } else {
            return new ItemStack(null,count);
        }
    }

    public BufferedImage readTexture() {
        int width = readInt();
        int height = readInt();
        BufferedImage bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);

        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                bufferedImage.setRGB(x,y,readInt());
            }
        }
        return bufferedImage;
    }

    public String[] readStrings() {
        int length = readShort();
        String[] values = new String[length];
        for(int x = 0; x < length; x++) {
            values[x] = readString();
        }
        return values;
    }

    public int[] readInts() {
        int length = readInt();
        int[] values = new int[length];
        for(int x = 0; x < length; x++) {
            values[x] = readInt();
        }
        return values;
    }

    public float[] readFloats() {
        int length = readInt();
        float[] values = new float[length];
        for(int x = 0; x < length; x++) {
            values[x] = readFloat();
        }
        return values;
    }

    public short[] readShorts() {
        int length = readInt();
        short[] values = new short[length];
        for(int x = 0; x < length; x++) {
            values[x] = readShort();
        }
        return values;
    }

    public byte[] readBytes() {
        int length = readInt();
        byte[] values = new byte[length];
        for(int x = 0; x < length; x++) {
            values[x] = readByte();
        }
        return values;
    }

    public long[] readLongs() {
        int length = readInt();
        long[] values = new long[length];
        for(int x = 0; x < length; x++) {
            values[x] = readLong();
        }
        return values;
    }

    public double[] readDoubles() {
        int length = readInt();
        double[] values = new double[length];
        for(int x = 0; x < length; x++) {
            values[x] = readDouble();
        }
        return values;
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

    public void writeLong(long val) {
        size += 8;
        byteBuf.writeLong(val);
    }

    public void writeDouble(double val) {
        size += 8;
        byteBuf.writeDouble(val);
    }

    public void writeString(String val) {
        if(val.length() == 0) {
            writeShort((short)-1);
            return;
        }
        short stringLength = (short) val.length();
        writeShort(stringLength);

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
        writeInt(itemStack.count);
    }

    public void writeTexture(BufferedImage bufferedImage) {
        writeInt(bufferedImage.getWidth());
        writeInt(bufferedImage.getHeight());
        for(int x = 0; x < bufferedImage.getWidth(); x++) {
            for(int y = 0; y < bufferedImage.getHeight(); y++) {
                writeInt(bufferedImage.getRGB(x,y));
            }
        }
    }

    public void writeStrings(String[] strings) {
        writeShort((short) strings.length);
        for(String string : strings) {
            writeString(string);
        }
    }

    public void writeInts(int[] vals) {
        writeInt(vals.length);
        for(int val : vals) {
            writeInt(val);
        }
    }

    public void writeFloats(float[] vals) {
        writeInt(vals.length);
        for(float val : vals) {
            writeFloat(val);
        }
    }

    public void writeShorts(short[] vals) {
        writeInt(vals.length);
        for(short val : vals) {
            writeShort(val);
        }
    }

    public void writeBytes(byte[] vals) {
        writeInt(vals.length);
        for(byte val : vals) {
            writeByte(val);
        }
    }

    public void writeLongs(long[] vals) {
        writeInt(vals.length);
        for(long val : vals) {
            writeLong(val);
        }
    }

    public void writeDoubles(double[] vals) {
        writeInt(vals.length);
        for(double val : vals) {
            writeDouble(val);
        }
    }

}
