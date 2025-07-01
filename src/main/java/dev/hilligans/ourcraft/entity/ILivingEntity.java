package dev.hilligans.ourcraft.entity;

import dev.hilligans.ourcraft.data.other.IInventory;

public interface ILivingEntity extends IEntity, IInventory {

    int getHealth();

    void setHealth(int health);

    void dealDamage(int dmg);

    void heal(int amount);

    boolean alive();
}
