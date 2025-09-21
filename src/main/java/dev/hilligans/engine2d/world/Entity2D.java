package dev.hilligans.engine2d.world;

import dev.hilligans.engine.data.IBoundingBox;
import dev.hilligans.ourcraft.entity.EntityType;
import dev.hilligans.ourcraft.entity.IEntity;

public class Entity2D implements IEntity {

    public double x, y, z;
    public long id;
    public EntityType entityType;

    public Entity2D(EntityType entityType) {
        this.entityType = entityType;
    }

    @Override
    public void setRot(float pitch, float yaw, float roll) {
    }

    @Override
    public long getID() {
        return id;
    }

    @Override
    public void setID(long id) {
        this.id = id;
    }

    @Override
    public EntityType getEntityType() {
        return entityType;
    }

    @Override
    public IBoundingBox getEntityBoundingBox() {
        return null;
    }

    @Override
    public float getPitch() {
        return 0;
    }

    @Override
    public float getYaw() {
        return 0;
    }

    @Override
    public float getRoll() {
        return 0;
    }

    @Override
    public void setVel(float velX, float velY, float velZ) {}

    @Override
    public float getVelX() {
        return 0;
    }

    @Override
    public float getVelY() {
        return 0;
    }

    @Override
    public float getVelZ() {
        return 0;
    }

    @Override
    public void setPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return 0;
    }

    @Override
    public void tick() {

    }
}
