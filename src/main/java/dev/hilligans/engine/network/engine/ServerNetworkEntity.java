package dev.hilligans.engine.network.engine;

import dev.hilligans.engine.application.IServerApplication;
import dev.hilligans.ourcraft.data.other.server.ServerPlayerData;

public interface ServerNetworkEntity<T extends IServerApplication> extends NetworkEntity {


    /**
     * @return the player data belonging to this player
     * @throws IllegalStateException - if the player data hasn't been loaded yet in the networking sequence
     */
    ServerPlayerData getServerPlayerData();

    void setServerPlayerData(ServerPlayerData data);

    T getServer();
}
