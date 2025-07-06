package dev.hilligans.ourcraft.network.engine;

import dev.hilligans.ourcraft.data.other.server.ServerPlayerData;
import dev.hilligans.ourcraft.entity.living.entities.PlayerEntity;
import dev.hilligans.ourcraft.server.IServer;
import dev.hilligans.ourcraft.world.newworldsystem.IServerWorld;

public interface ServerNetworkEntity extends NetworkEntity {

    /**
     * @return the world in which the player is currently in
     * @throws IllegalStateException - if the player data hasn't been loaded yet in the networking sequence
     */
    IServerWorld getServerWorld();

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

}
