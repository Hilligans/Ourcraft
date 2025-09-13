package dev.hilligans.ourcraft.entity;

import dev.hilligans.engine.data.IBoundingBox;
import dev.hilligans.ourcraft.world.newworldsystem.IWorld;

public class NewEntity implements IEntity {

    public IWorld world;
    public EntityType entityType;
    public long entityID;

    public float pitch, yaw, velX, velY, velZ;
    public double x, y, z;

    public IBoundingBox boundingBox = IBoundingBox.EMPTY_BOX;

    public NewEntity(EntityType entityType) {
        this.entityType = entityType;
    }

    @Override
    public IWorld getWorld() {
        return this.world;
    }

    @Override
    public void setWorld(IWorld world) {
        this.world = world;
    }

    @Override
    public void setRot(float pitch, float yaw, float roll) {
        this.pitch = pitch;
        this.yaw = yaw;
    }

    @Override
    public long getID() {
        return entityID;
    }

    @Override
    public void setID(long id) {
        this.entityID = id;
    }

    @Override
    public EntityType getEntityType() {
        return entityType;
    }

    @Override
    public IBoundingBox getEntityBoundingBox() {
        return boundingBox;
    }

    @Override
    public float getPitch() {
        return pitch;
    }

    @Override
    public float getYaw() {
        return yaw;
    }

    @Override
    public float getRoll() {
        return 0;
    }

    @Override
    public void setVel(float velX, float velY, float velZ) {
        this.velX = velX;
        this.velY = velY;
        this.velZ = velZ;
    }

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
        return velZ;
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
        return z;
    }

    @Override
    public void tick() {

    }
}
