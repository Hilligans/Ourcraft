package dev.hilligans.ourcraft.entity;

import dev.hilligans.ourcraft.data.other.server.ServerPlayerData;
import dev.hilligans.ourcraft.server.IServer;

public interface IPlayerEntity extends IEntity {

    String getName();

    ServerPlayerData getPlayerData();
}
