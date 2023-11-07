package dev.hilligans.ourcraft.entity;

import dev.hilligans.ourcraft.network.IPacketByteArray;

public abstract class LivingEntity extends Entity {

    public int maxHealth;
    public int health;

    public LivingEntity(float x, float y, float z, int id, int maxHealth) {
        super(x, y, z, id);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    @Override
    public void tick() {
        move();
    }

    public LivingEntity(IPacketByteArray packetData) {
        super(packetData);
    }

    public void hitGround(float vel) {
        float amount = (int)(vel * 10);
        health -= amount;
    }


}
