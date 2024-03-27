package dev.hilligans.ourcraft.network;

import dev.hilligans.ourcraft.network.debug.PacketTraceByteArray;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

public interface IPacketHandler {

    default ChannelFuture sendPacket(PacketBase<?> packetBase, ChannelHandlerContext ctx) {
        packetBase.packetId = getNetwork().sendProtocol.packetMap.get(packetBase.getClass());
        return ctx.channel().writeAndFlush(new PacketByteArray(packetBase));
    }

    Network getNetwork();
}
