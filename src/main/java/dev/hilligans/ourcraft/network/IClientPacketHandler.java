package dev.hilligans.ourcraft.network;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.world.newworldsystem.IWorld;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

public interface IClientPacketHandler extends IPacketHandler {

    void sendPacket(PacketBase<?> packetBase);

    Client getClient();

    IWorld getWorld();

    GameInstance getGameInstance();
}
