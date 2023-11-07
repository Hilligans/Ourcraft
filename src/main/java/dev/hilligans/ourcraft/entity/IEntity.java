package dev.hilligans.ourcraft.entity;

import dev.hilligans.ourcraft.world.newworldsystem.IWorld;

public interface IEntity {

    IWorld getWorld();
    void setWorld(IWorld world);

    void setRot(float pitch, float yaw);
    void setVel(float velX, float velY, float velZ);






}
