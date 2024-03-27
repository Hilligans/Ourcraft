package dev.hilligans.ourcraft.util;

import dev.hilligans.ourcraft.save.IEncodeable;
import dev.hilligans.ourcraft.tag.INBTTag;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public interface IByteArray extends INBTTag {

    @Override
    default long getSize() {
        return length();
    }

    @Override
    default int getType() {
        return 7; //byte array
    }



    long length();
    long readerIndex();

    void setReaderIndex(long index);

    default boolean readBoolean() {
        return readByte() != 0;
    }
    byte readByte();
    short readShort();
    default char readChar() {
        return (char) readShort();
    }
    int readInt();
    long readLong();
    float readFloat();
    double readDouble();

    default String readUTF8() {
        return new String(readBytes(), StandardCharsets.UTF_8);
    }
    default String readUTF16() {
        return new String(readBytes(), StandardCharsets.UTF_16);
    }

    default int readVarInt() {
        int numRead = 0;
        int result = 0;
        byte read;
        do {
            read = readByte();
            int value = (read & 0b01111111);
            result |= (value << (7 * numRead));

            numRead++;
            if (numRead > 5) {
                throw new RuntimeException("VarInt is too big");
            }
        } while ((read & 0b10000000) != 0);

        return result;
    }

    default long readVarLong() {
        int numRead = 0;
        long result = 0;
        byte read;
        do {
            read = readByte();
            long value = (read & 0b01111111);
            result |= (value << (7 * numRead));

            numRead++;
            if (numRead > 10) {
                throw new RuntimeException("VarLong is too big");
            }
        } while ((read & 0b10000000) != 0);

        return result;
    }

    default int readUByte() {
        return Byte.toUnsignedInt(readByte());
    }

    default int readUShort() {
        return Short.toUnsignedInt(readShort());
    }

    default long readUInt() {
        return Integer.toUnsignedLong(readInt());
    }

    default boolean[] readBooleans(int count) {
        boolean[] values = new boolean[count];
        byte val = 0;
        for(int x = 0; x < count; x++) {
            if(x % 8 == 0) {
                val = readByte();
            }
            values[x] = (val & 0b1) == 1;
            val >>= 1;
        }
        return values;
    }

    default byte[] readBytes(int count) {
        byte[] values = new byte[count];
        for(int x = 0; x < count; x++) {
            values[x] = readByte();
        }
        return values;
    }

    default short[] readShorts(int count) {
        short[] values = new short[count];
        for(int x = 0; x < count; x++) {
            values[x] = readShort();
        }
        return values;
    }

    default char[] readChars(int count) {
        char[] values = new char[count];
        for(int x = 0; x < count; x++) {
            values[x] = readChar();
        }
        return values;
    }

    default int[] readInts(int count) {
        int[] values = new int[count];
        for(int x = 0; x < count; x++) {
            values[x] = readInt();
        }
        return values;
    }

    default long[] readLongs(int count) {
        long[] values = new long[count];
        for(int x = 0; x < count; x++) {
            values[x] = readLong();
        }
        return values;
    }

    default float[] readFloats(int count) {
        float[] values = new float[count];
        for(int x = 0; x < count; x++) {
            values[x] = readFloat();
        }
        return values;
    }

    default double[] readDoubles(int count) {
        double[] values = new double[count];
        for(int x = 0; x < count; x++) {
            values[x] = readDouble();
        }
        return values;
    }

    default String[] readUTF8s(int count) {
        String[] values = new String[count];
        for(int x = 0; x < count; x++) {
            values[x] = readUTF8();
        }
        return values;
    }

    default String[] readUTF16s(int count) {
        String[] values = new String[count];
        for(int x = 0; x < count; x++) {
            values[x] = readUTF16();
        }
        return values;
    }

    default int[] readVarInts(int count) {
        int[] values = new int[count];
        for(int x = 0; x < count; x++) {
            values[x] = readVarInt();
        }
        return values;
    }

    default long[] readVarLongs(int count) {
        long[] values = new long[count];
        for(int x = 0; x < count; x++) {
            values[x] = readVarLong();
        }
        return values;
    }


    default boolean[] readBooleans() {
        return readBooleans(readVarInt());
    }

    default byte[] readBytes() {
        return readBytes(readVarInt());
    }

    default short[] readShorts() {
        return readShorts(readVarInt());
    }

    default char[] readChars() {
        return readChars(readVarInt());
    }

    default int[] readInts() {
        return readInts(readVarInt());
    }

    default long[] readLongs() {
        return readLongs(readVarInt());
    }

    default float[] readFloats() {
        return readFloats(readVarInt());
    }

    default double[] readDoubles() {
        return readDoubles(readVarInt());
    }

    default String[] readUTF8s() {
        return readUTF8s(readVarInt());
    }

    default String[] readUTF16s() {
        return readUTF16s(readVarInt());
    }

    default int[] readVarInts() {
        return readVarInts(readVarInt());
    }

    default long[] readVarLongs() {
        return readVarLongs(readVarInt());
    }


    default void writeBoolean(boolean val) {
        writeByte((byte) (val ? 1 : 0));
    }
    void writeByte(byte val);
    void writeShort(short val);
    default void writeChar(char val) {
        writeShort((short) val);
    }
    void writeInt(int val);
    void writeLong(long val);
    void writeFloat(float val);
    void writeDouble(double val);

    default void writeUTF8(String val) {
        writeBytes(val.getBytes(StandardCharsets.UTF_8));
    }
    default void writeUTF16(String val) {
        writeBytes(val.getBytes(StandardCharsets.UTF_16));
    }

    default void writeVarInt(int value) {
        while (true) {
            if ((value & 0xFFFFFF80) == 0) {
                writeByte((byte) value);
                return;
            }

            writeByte((byte) (value & 0x7F | 0x80));
            value >>>= 7;
        }
    }

    default void writeVarLong(long value) {
        while (true) {
            if ((value & 0xFFFFFFFFFFFFFF80L) == 0) {
                writeByte((byte) value);
                return;
            }

            writeByte((byte) (value & 0x7F | 0x80));
            value >>>= 7;
        }
    }

    default void writeUByte(int value) {
        writeByte((byte)value);
    }

    default void writeUShort(int value) {
        writeShort((short)value);
    }

    default void writeUInt(long value) {
        writeInt((int)value);
    }

    default void writeBooleans(boolean[] values) {
        writeVarInt(values.length);
        byte val = 0;
        for(int x = 0; x < values.length; x++) {
            if((x + 1) % 8 == 0) {
                writeByte(val);
            }
            val |= (byte) (values[x] ? 0b10000000 : 0);
            val >>= 1;
        }
        if(values.length % 8 == 0) {
            writeByte(val);
        }
    }

    default void writeBytes(byte[] values) {
        writeVarInt(values.length);
        for(int x = 0; x < values.length; x++) {
            writeByte(values[x]);
        }
    }

    default void writeShorts(short[] values) {
        writeVarInt(values.length);
        for(int x = 0; x < values.length; x++) {
            writeShort(values[x]);
        }
    }

    default void writeChars(char[] values) {
        writeVarInt(values.length);
        for(int x = 0; x < values.length; x++) {
            writeChar(values[x]);
        }
    }

    default void writeInts(int[] values) {
        writeVarInt(values.length);
        for(int x = 0; x < values.length; x++) {
            writeInt(values[x]);
        }
    }

    default void writeLongs(long[] values) {
        writeVarInt(values.length);
        for (int x = 0; x < values.length; x++) {
            writeLong(values[x]);
        }
    }

    default void writeFloats(float[] values) {
        writeVarInt(values.length);
        for(int x = 0; x < values.length; x++) {
            writeFloat(values[x]);
        }
    }

    default void writeDoubles(double[] values) {
        writeVarInt(values.length);
        for(int x = 0; x < values.length; x++) {
            writeDouble(values[x]);
        }
    }

    default void writeUTF8s(String[] values) {
        writeVarInt(values.length);
        for(int x = 0; x < values.length; x++) {
            writeUTF8(values[x]);
        }
    }

    default void writeUTF16s(String[] values) {
        writeVarInt(values.length);
        for(int x = 0; x < values.length; x++) {
            writeUTF16(values[x]);
        }
    }

    default void writeVarInts(int[] values) {
        writeVarInt(values.length);
        for(int x = 0; x < values.length; x++) {
            writeVarInt(values[x]);
        }
    }

    default void writeVarLongs(long[] values) {
        writeVarInt(values.length);
        for(int x = 0; x < values.length; x++) {
            writeVarLong(values[x]);
        }
    }

    default void write(List<IEncodeable> list) {
        writeVarInt(list.size());
        for(IEncodeable encodeable : list) {
            encodeable.write(this);
        }
    }

    default  <T extends IEncodeable> List<T> read(Supplier<T> encodeableSupplier) {
        int size = readVarInt();
        ArrayList<T> list = new ArrayList<>(size);
        for(int x = 0; x < size; x++) {
            T encodable = encodeableSupplier.get();
            encodable.read(this);
            list.add(encodable);
        }
        return list;
    }

    default void writeString(String string) {
        writeUTF16(string);
    }

    default String readString() {
        return readUTF16();
    }
}