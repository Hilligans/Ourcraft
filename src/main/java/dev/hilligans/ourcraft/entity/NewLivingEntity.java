package dev.hilligans.ourcraft.entity;

public class NewLivingEntity extends NewEntity implements ILivingEntity {

    public int maxHealth;

    public int health;

    public NewLivingEntity(EntityType entityType) {
        super(entityType);
    }

    @Override
    public int getHealth() {
        return maxHealth;
    }

    @Override
    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public void dealDamage(int dmg) {
        health -= dmg;
        if(health < 0) {
            health = 0;
        }
    }

    @Override
    public void heal(int amount) {
        health += amount;
        if(health > maxHealth) {
            health = maxHealth;
        }
    }

    @Override
    public boolean alive() {
        return health > 0;
    }
}
