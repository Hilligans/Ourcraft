package dev.hilligans.ourcraft.network;

import io.netty.channel.ChannelHandlerContext;

public abstract class PacketBase<T extends IPacketHandler> {

    public ChannelHandlerContext ctx;
    public int packetId;

    public PacketBase() {}

    public PacketBase(int packetID) {
        this.packetId = packetID;
    }

    public abstract void encode(IPacketByteArray packetData);

    public abstract void decode(IPacketByteArray packetData);

    public abstract void handle(T t);

    public void handle(Object t) {
        this.handle((T)t);
    }
}
