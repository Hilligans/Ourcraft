package dev.hilligans.ourcraft.entity;

public class NewPlayerEntity extends NewLivingEntity implements IPlayerEntity {

    public String name;

    public NewPlayerEntity(EntityType entityType) {
        super(entityType);
    }

    @Override
    public String getName() {
        return name;
    }
}
