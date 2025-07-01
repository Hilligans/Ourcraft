package dev.hilligans.ourcraft.entity;

import dev.hilligans.ourcraft.data.other.server.IInventoryChanged;
import dev.hilligans.ourcraft.item.ItemStack;

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

    @Override
    public int getInventorySize() {
        return 0;
    }

    @Override
    public ItemStack getItem(int slot) {
        return null;
    }

    @Override
    public void setItem(int slot, ItemStack item) {

    }

    @Override
    public boolean addItem(ItemStack itemStack) {
        return false;
    }

    @Override
    public void addListener(int slot, IInventoryChanged iInventoryChanged) {

    }

    @Override
    public void removeListener(int slot, IInventoryChanged iInventoryChanged) {

    }

    @Override
    public void notifyListeners(int slot) {

    }
}
