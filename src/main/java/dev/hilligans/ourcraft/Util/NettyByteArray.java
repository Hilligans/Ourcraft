package dev.hilligans.ourcraft.Util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledHeapByteBuf;
import io.netty.util.ByteProcessor;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class NettyByteArray implements IByteArray {

    public ByteBuf byteBuf;
    public int index = 0;

    public NettyByteArray(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
        this.index = byteBuf.readableBytes();
    }

    public NettyByteArray(int size) {
        byteBuf = new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT, size, size);
    }

    public NettyByteArray() {
    }

    @Override
    public void write(MemorySegment memorySegment) {
        ValueLayout.OfByte ofByte = ValueLayout.JAVA_BYTE;
        byteBuf.forEachByte(new ByteProcessor() {
            int x = 0;
            @Override
            public boolean process(byte b) {
                memorySegment.set(ofByte, x++, b);
                return memorySegment.byteSize() > x;
            }
        });
    }

    @Override
    public void read(MemorySegment memorySegment) {
        long size = memorySegment.byteSize();
        ValueLayout.OfByte ofByte = ValueLayout.JAVA_BYTE;
        for(long x = 0; x < size; x++) {
            writeByte(memorySegment.get(ofByte, x));
        }
    }

    @Override
    public long length() {
        return byteBuf.readableBytes();
    }

    @Override
    public long readerIndex() {
        return index;
    }

    @Override
    public void setReaderIndex(long index) {
        this.index = (int) index;
    }

    @Override
    public byte readByte() {
        index -= 1;
        return byteBuf.readByte();
    }

    @Override
    public short readShort() {
        index -= 2;
        return byteBuf.readShort();
    }

    @Override
    public int readInt() {
        index -= 4;
        return byteBuf.readInt();
    }

    @Override
    public long readLong() {
        index -= 8;
        return byteBuf.readLong();
    }

    @Override
    public float readFloat() {
        index -= 4;
        return byteBuf.readFloat();
    }

    @Override
    public double readDouble() {
        index -= 8;
        return byteBuf.readDouble();
    }

    @Override
    public String readUTF8() {
        return byteBuf.readCharSequence(readVarInt(), StandardCharsets.UTF_8).toString();
    }

    @Override
    public String readUTF16() {
        int length = readVarInt();
        System.out.print("read length:" + length + " ");
        String s = byteBuf.readCharSequence(length, StandardCharsets.UTF_16).toString();
        System.out.println(s);
        return s;
    }

    @Override
    public void writeByte(byte val) {
        index += 1;
        byteBuf.writeByte(val);
    }

    @Override
    public void writeShort(short val) {
        index += 2;
    }

    @Override
    public void writeInt(int val) {
        index += 4;
    }

    @Override
    public void writeLong(long val) {
        index += 8;
    }

    @Override
    public void writeFloat(float val) {
        index += 4;
    }

    @Override
    public void writeDouble(double val) {
        index += 8;
    }

    @Override
    public void writeUTF8(String val) {
        writeVarInt(val.length());
        byteBuf.writeCharSequence(val, StandardCharsets.UTF_8);
    }

    @Override
    public void writeUTF16(String val) {
        int length = val.length();
        System.out.println("Length:" + length + " " + val);
        writeVarInt(val.length());
        byteBuf.writeCharSequence(val, StandardCharsets.UTF_16);
    }
}
