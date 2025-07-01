package dev.hilligans.ourcraft.entity;

import dev.hilligans.ourcraft.data.other.IBoundingBox;
import dev.hilligans.ourcraft.world.newworldsystem.IWorld;

public interface IEntity {

    IWorld getWorld();
    void setWorld(IWorld world);

    void setRot(float pitch, float yaw, float roll);

    long getID();
    void setID(long id);

    EntityType getEntityType();

    IBoundingBox getEntityBoundingBox();

    float getPitch();
    float getYaw();
    float getRoll();

    void setVel(float velX, float velY, float velZ);
    float getVelX();
    float getVelY();
    float getVelZ();

    void setPosition(double x, double y, double z);

    double getX();
    double getY();
    double getZ();

    void tick();

    default boolean intersects(IEntity entity) {
        return entity.getEntityBoundingBox().intersects(getEntityBoundingBox(), entity.getX(), entity.getY(), entity.getZ(), this.getX(), this.getY(), this.getZ());
    }
}
