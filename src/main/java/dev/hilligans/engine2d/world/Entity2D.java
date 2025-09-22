package dev.hilligans.engine2d.world;

import dev.hilligans.engine.data.BoundingBox;
import dev.hilligans.engine.data.IBoundingBox;
import dev.hilligans.engine.entity.EntityType;
import dev.hilligans.engine.entity.IEntity;

public class Entity2D implements IEntity {

    public double x, y;
    public long id;
    public EntityType entityType;
    public float velX, velY;
    public float width, height;
    public World2D world2D;

    public Entity2D(EntityType entityType) {
        this.entityType = entityType;
        this.width = 32;
        this.height = 32;
    }

    public void setWorld(World2D world) {
        this.world2D = world;
    }

    public World2D getWorld() {
        return world2D;
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
        return new BoundingBox(x, y, -1, x + width, y + height, 1);
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
        return velX;
    }

    @Override
    public float getVelY() {
        return velY;
    }

    @Override
    public float getVelZ() {
        return 0;
    }

    @Override
    public void setPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
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
