package dev.hilligans.ourcraft.entity;

import dev.hilligans.ourcraft.data.other.server.ServerPlayerData;
import dev.hilligans.ourcraft.server.IServer;

public interface IPlayerEntity extends ILivingEntity {

    String getName();

    ServerPlayerData getPlayerData();
}
