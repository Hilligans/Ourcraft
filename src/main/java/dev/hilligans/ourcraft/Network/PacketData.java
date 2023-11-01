package dev.hilligans.ourcraft.Network;

import dev.hilligans.ourcraft.Util.NettyByteArray;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.nio.ByteBuffer;
import java.util.zip.Deflater;

public class PacketData extends NettyByteArray {


    public ChannelHandlerContext ctx;
    public short packetId = 0;

    public PacketData(PacketBase packetBase) {
        byteBuf = Unpooled.buffer();
        packetId = (short) packetBase.packetId;
        packetBase.encode(this);
    }

    public PacketData(int val) {
        byteBuf = Unpooled.buffer();
        byteBuf.writeByte(val);
    }

    public PacketData(byte[] bytes, int packetWidth) {
        byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(bytes);
        packetId = byteBuf.readShort();
        index = bytes.length;
    }

    public PacketData(byte[] bytes) {
        byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(bytes);
        index = bytes.length;
    }

    public PacketData(ByteBuf byteBuf) {
        packetId =  byteBuf.readShort();
        byteBuf.readBytes(packetId);
        index = byteBuf.readableBytes();
    }

    public PacketData(ByteBuffer buffer, int packetWidth) {
        this(buffer.array(),packetWidth);
    }

    public void writeToByteBuf(ByteBuf byteBuf, int packetWidth, boolean compressed) {
        byteBuf.writeInt(index + 4 + packetWidth);
        if (compressed) {
            Deflater compressor = new Deflater();
            ByteBuffer buffer;
            if (packetWidth == 1) {
                buffer = this.byteBuf.nioBuffer(1, this.index);
                buffer.put(0, (byte) packetId);
            } else {
                buffer = this.byteBuf.nioBuffer(2, this.index);
                buffer.putShort(0, packetId);
            }
            compressor.setInput(buffer);
            compressor.finish();
            ByteBuffer newBuffer = ByteBuffer.allocate(4);
            int length = compressor.deflate(newBuffer);
            compressor.end();
            byteBuf.writeIntLE(length);
            byteBuf.writeBytes(newBuffer);
        } else {
            if (packetWidth == 1) {
                byteBuf.writeByte(packetId);
            } else {
                byteBuf.writeShort(packetId);
            }
            byteBuf.writeBytes(this.byteBuf);
        }
    }

    public PacketBase createPacket(Protocol protocol) {
        PacketBase packetBase = protocol.packets.get(packetId).getPacket();
        packetBase.ctx = ctx;
        packetBase.decode(this);
        return packetBase;
    }
}
