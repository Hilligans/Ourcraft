package dev.hilligans.ourcraft.entity;

import dev.hilligans.engine.entity.EntityType;
import dev.hilligans.ourcraft.data.other.server.ServerPlayerData;

public class NewPlayerEntity extends NewLivingEntity implements IPlayerEntity {

    public String name;
    public ServerPlayerData playerData;

    public NewPlayerEntity(EntityType entityType) {
        super(entityType);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ServerPlayerData getPlayerData() {
        return playerData;
    }
}
