package dev.hilligans.ourcraft.entity;

import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.data.IBoundingBox;
import dev.hilligans.ourcraft.world.newworldsystem.IWorld;

public interface IEntity {

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
    default void setPosition(double x, double y) {
        this.setPosition(x, y, 0);
    }

    double getX();
    double getY();
    double getZ();

    void tick();
    default void tickVisuals(GraphicsContext graphicsContext) {}

    default boolean intersects(IEntity entity) {
        return entity.getEntityBoundingBox().intersects(getEntityBoundingBox(), entity.getX(), entity.getY(), entity.getZ(), this.getX(), this.getY(), this.getZ());
    }
}
