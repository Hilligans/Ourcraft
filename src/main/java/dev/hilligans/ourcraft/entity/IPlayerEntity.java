package dev.hilligans.ourcraft.entity;

import dev.hilligans.ourcraft.data.other.server.ServerPlayerData;

public interface IPlayerEntity extends ILivingEntity {

    String getName();

    ServerPlayerData getPlayerData();
}
