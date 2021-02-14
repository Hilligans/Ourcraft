package Hilligans.Entity;

import Hilligans.Network.PacketData;

public abstract class LivingEntity extends Entity {

    public int maxHealth;
    public int health;

    public LivingEntity(float x, float y, float z, int id, int maxHealth) {
        super(x, y, z, id);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    public LivingEntity(PacketData packetData) {
        super(packetData);
    }


}
