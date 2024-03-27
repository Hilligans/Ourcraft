package dev.hilligans.ourcraft.network;

import dev.hilligans.ourcraft.util.IByteArray;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface IPacketByteArray extends IByteArray {

    ByteBuf getByteBuf();
    int getPacketID();
    void setOwner(ChannelHandlerContext ctx);
    ChannelHandlerContext getOwner();

    default PacketBase<?> createPacket(Protocol protocol) {
        PacketBase<?> packetBase = protocol.packets.get(getPacketID()).getPacket();
        packetBase.ctx = getOwner();
        packetBase.decode(this);
        return packetBase;
    }
}
