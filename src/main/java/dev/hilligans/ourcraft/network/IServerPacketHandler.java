package dev.hilligans.ourcraft.network;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.data.other.server.ServerPlayerData;
import dev.hilligans.ourcraft.entity.living.entities.PlayerEntity;
import dev.hilligans.ourcraft.network.debug.PacketTraceByteArray;
import dev.hilligans.ourcraft.server.IServer;
import dev.hilligans.ourcraft.world.newworldsystem.IServerWorld;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

public interface IServerPacketHandler extends IPacketHandler {

    /**
     * @return the world in which the player is currently in
     * @throws IllegalStateException - if the player data hasn't been loaded yet in the networking sequence
     */
    IServerWorld getWorld();

    /**
     * @return the player data belonging to this player
     * @throws IllegalStateException - if the player data hasn't been loaded yet in the networking sequence
     */
    ServerPlayerData getServerPlayerData();

    /**
     * @return the player entity belonging to this player
     * @throws IllegalStateException - if the player data hasn't been loaded yet in the networking sequence
     */
    PlayerEntity getPlayerEntity();

    IServer getServer();

    ServerNetworkHandler getServerNetworkHandler();

    void disconnect(String reason);

    default GameInstance getGameInstance() {
        return getServer().getGameInstance();
    }

    default void handleDisconnect() {
        getWorld().removeEntity(getPlayerEntity().id, 0);
    }

    default ChannelFuture sendPacket(PacketBase<?> packetBase, ChannelHandlerContext ctx) {
        if(ServerNetworkHandler.debug) {
            return ctx.channel().writeAndFlush(new PacketTraceByteArray(packetBase));
        } else {
            return ctx.channel().writeAndFlush(new PacketByteArray(packetBase));
        }
    }
    
    default void sendPacket(PacketBase<?> packetBase) {
        getServerNetworkHandler().sendPacketInternal(packetBase);
    }
}
