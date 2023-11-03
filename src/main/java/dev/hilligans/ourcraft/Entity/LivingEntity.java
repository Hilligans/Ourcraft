package dev.hilligans.ourcraft.Entity;

import dev.hilligans.ourcraft.Network.IPacketByteArray;
import dev.hilligans.ourcraft.Network.PacketData;

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
