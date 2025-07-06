package dev.hilligans.ourcraft.network;

import dev.hilligans.ourcraft.network.debug.PacketTraceByteArray;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;

public interface IPacketHandler {

    default ChannelFuture sendPacket(PacketBase<?> packetBase, ChannelHandlerContext ctx) {
        //packetBase.packetId = getSendProtocol(ctx).packetMap.get(packetBase.getClass());
        return ctx.channel().writeAndFlush(new PacketByteArray(packetBase));
    }
}
