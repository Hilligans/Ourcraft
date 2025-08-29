package dev.hilligans.ourcraft.network;

import dev.hilligans.ourcraft.util.IByteArray;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ByteProcessor;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class PacketByteArray implements IPacketByteArray {

    public ByteBuf byteBuf;
    public int packetID;
    public int index = 0;
    ChannelHandlerContext ctx;

    public PacketByteArray() {
        byteBuf = Unpooled.buffer();
    }

    public PacketByteArray(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
        this.index = byteBuf.readableBytes();
    }

    public PacketByteArray(PacketBase<?> packetBase) {
        byteBuf = Unpooled.buffer();
        packetID = (short) packetBase.packetId;
        packetBase.encode(this);
    }

    public PacketByteArray(byte[] bytes) {
        byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(bytes);
        index = bytes.length;
    }

    public PacketByteArray(byte[] bytes, int packetWidth) {
        byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(bytes);
        if(packetWidth == 2) {
            packetID = byteBuf.readShort();
        } else if(packetWidth == 1) {
            packetID = byteBuf.readByte();
        } else if(packetWidth == 4) {
            packetID = byteBuf.readInt();
        } else {
            throw new RuntimeException("Unknown packet id width: " + packetWidth);
        }
        index = bytes.length - packetWidth;
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
        return index;
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
    public void writeByte(byte val) {
        index += 1;
        byteBuf.writeByte(val);
    }

    @Override
    public void writeShort(short val) {
        index += 2;
        byteBuf.writeShort(val);
    }

    @Override
    public void writeInt(int val) {
        index += 4;
        byteBuf.writeInt(val);
    }

    @Override
    public void writeLong(long val) {
        index += 8;
        byteBuf.writeLong(val);
    }

    @Override
    public void writeFloat(float val) {
        index += 4;
        byteBuf.writeFloat(val);
    }

    @Override
    public void writeDouble(double val) {
        index += 8;
        byteBuf.writeDouble(val);
    }

    @Override
    public byte peekByte() {
        byteBuf.markReaderIndex();
        byte val = byteBuf.readByte();
        byteBuf.resetReaderIndex();
        return val;
    }

    @Override
    public short peekShort() {
        byteBuf.markReaderIndex();
        short val = byteBuf.readShort();
        byteBuf.resetReaderIndex();
        return val;
    }

    @Override
    public int peekInt() {
        byteBuf.markReaderIndex();
        int val = byteBuf.readInt();
        byteBuf.resetReaderIndex();
        return val;
    }

    @Override
    public long peekLong() {
        byteBuf.markReaderIndex();
        long val = byteBuf.readLong();
        byteBuf.resetReaderIndex();
        return val;
    }

    @Override
    public float peekFloat() {
        byteBuf.markReaderIndex();
        float val = byteBuf.readFloat();
        byteBuf.resetReaderIndex();
        return val;
    }

    @Override
    public double peekDouble() {
        byteBuf.markReaderIndex();
        double val = byteBuf.readDouble();
        byteBuf.resetReaderIndex();
        return val;
    }

    @Override
    public ByteBuf getByteBuf() {
        return byteBuf;
    }

    @Override
    public int getPacketID() {
        return packetID;
    }

    @Override
    public void setOwner(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public ChannelHandlerContext getOwner() {
        return ctx;
    }
}
