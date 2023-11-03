package dev.hilligans.ourcraft.Network.Debug;

import dev.hilligans.ourcraft.Network.IPacketByteArray;
import dev.hilligans.ourcraft.Network.PacketBase;
import dev.hilligans.ourcraft.Network.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledHeapByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ByteProcessor;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

public class PacketTraceByteArray implements IPacketByteArray {

    public ByteBuf byteBuf;
    public int index = 0;
    public int packetID;
    public PacketTrace packetTrace = new PacketTrace();
    ChannelHandlerContext ctx;
    public long universalPacketID;


    public PacketTraceByteArray(PacketBase packetBase) {
        byteBuf = Unpooled.buffer();
        packetID = (short) packetBase.packetId;
        packetBase.encode(this);
        universalPacketID = PacketTrace.PACKET_IDS.getAndIncrement();
    }

    public PacketTraceByteArray(byte[] bytes, int packetWidth, PacketTrace packetTrace) {
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
        this.packetTrace = packetTrace;
        index = bytes.length - packetWidth;
    }

    public PacketTraceByteArray() {
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
        return packetTrace.get(byteBuf.readByte());
    }

    @Override
    public short readShort() {
        index -= 2;
        return packetTrace.get(byteBuf.readShort());
    }

    @Override
    public int readInt() {
        index -= 4;
        return packetTrace.get(byteBuf.readInt());
    }

    @Override
    public long readLong() {
        index -= 8;
        return packetTrace.get(byteBuf.readLong());
    }

    @Override
    public float readFloat() {
        index -= 4;
        return packetTrace.get(byteBuf.readFloat());
    }

    @Override
    public double readDouble() {
        index -= 8;
        return packetTrace.get(byteBuf.readDouble());
    }

    @Override
    public String readUTF8() {
        return byteBuf.readCharSequence(readVarInt(), StandardCharsets.UTF_8).toString();
    }

    @Override
    public String readUTF16() {
        int length = readVarInt();
        //System.out.print("read length:" + length + " ");
        String s = byteBuf.readCharSequence(length, StandardCharsets.UTF_16).toString();
        //System.out.println(s);
        return s;
    }

    @Override
    public void writeByte(byte val) {
        index += 1;
        byteBuf.writeByte(val);
        packetTrace.put(val);
    }

    @Override
    public void writeShort(short val) {
        index += 2;
        byteBuf.writeShort(val);
        packetTrace.put(val);
    }

    @Override
    public void writeInt(int val) {
        index += 4;
        byteBuf.writeInt(val);
        packetTrace.put(val);
    }

    @Override
    public void writeLong(long val) {
        index += 8;
        byteBuf.writeLong(val);
        packetTrace.put(val);
    }

    @Override
    public void writeFloat(float val) {
        index += 4;
        byteBuf.writeFloat(val);
        packetTrace.put(val);
    }

    @Override
    public void writeDouble(double val) {
        index += 8;
        byteBuf.writeDouble(val);
        packetTrace.put(val);
    }

    @Override
    public void writeUTF8(String val) {
        writeBytes(val.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void writeUTF16(String val) {
        writeBytes(val.getBytes(StandardCharsets.UTF_16));
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

    public long getUniversalPacketID() {
        return universalPacketID;
    }

    public PacketBase createPacket(Protocol protocol) {
        PacketBase packetBase = protocol.packets.get(getPacketID()).getPacket();
        packetBase.ctx = getOwner();
        //System.out.println("Packet Trace:" + packetTrace.data);
        packetBase.decode(this);
        return packetBase;
    }
}
