package dev.hilligans.ourcraft.network;

import io.netty.channel.ChannelHandlerContext;

public abstract class PacketBase {

    public ChannelHandlerContext ctx;

    public int packetId;

    public PacketBase(int id) {
        this.packetId = id;
    }

    public PacketBase() {}

    public abstract void encode(IPacketByteArray packetData);

    public abstract void decode(IPacketByteArray packetData);

    public abstract void handle();
}
