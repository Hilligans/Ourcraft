package dev.hilligans.ourcraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.nio.ByteBuffer;

public class PacketData extends PacketByteArray {


    public ChannelHandlerContext ctx;
    public short packetId = 0;

    public PacketData(PacketBase packetBase) {
        byteBuf = Unpooled.buffer();
        packetId = (short) packetBase.packetId;
        packetBase.encode(this);
    }

    public PacketData(byte[] bytes, int packetWidth) {
        byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(bytes);
        packetId = byteBuf.readShort();
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


}
