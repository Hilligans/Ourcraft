package dev.hilligans.ourcraft.Network;

import io.netty.channel.ChannelHandlerContext;

public abstract class PacketBase {

    public ChannelHandlerContext ctx;


    public int packetId;

    public PacketBase(int id) {
        this.packetId = id;
    }

    public PacketBase() {}

    public abstract void encode(PacketData packetData);

    public abstract void decode(PacketData packetData);

    public abstract void handle();
}
