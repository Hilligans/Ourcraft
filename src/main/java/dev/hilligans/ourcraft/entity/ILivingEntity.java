package dev.hilligans.ourcraft.entity;

public interface ILivingEntity extends IEntity {

    int getHealth();

    void setHealth(int health);

    void dealDamage(int dmg);

    void heal(int amount);

    boolean alive();
}
