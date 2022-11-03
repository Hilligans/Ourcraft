package dev.Hilligans.ourcraft.Network;

import dev.Hilligans.ourcraft.Network.Packet.InvalidFormatPacket;
import dev.Hilligans.ourcraft.Network.Packet.Client.*;
import dev.Hilligans.ourcraft.Network.Packet.Server.*;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.function.Supplier;

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
