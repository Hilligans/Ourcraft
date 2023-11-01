package dev.hilligans.ourcraft.Entity;

import dev.hilligans.ourcraft.World.NewWorldSystem.IWorld;

public interface IEntity {

    IWorld getWorld();
    void setWorld(IWorld world);

    void setRot(float pitch, float yaw);
    void setVel(float velX, float velY, float velZ);






}
