package dev.hilligans.ourcraft.command.executors;

import dev.hilligans.ourcraft.entity.Entity;
import dev.hilligans.ourcraft.world.newworldsystem.IWorld;
import dev.hilligans.ourcraft.world.newworldsystem.ServerCubicWorld;
import dev.hilligans.ourcraft.server.IServer;

public class EntityExecutor implements CommandExecutor {

    public Entity entity;

    public EntityExecutor(Entity entity) {
        this.entity = entity;
    }

    @Override
    public double getX() {
        return entity.getX();
    }

    @Override
    public double getY() {
        return entity.getY();
    }

    @Override
    public double getZ() {
        return 0;
    }

    @Override
    public IServer getServer() {
        IWorld world = entity.world;
        if(world instanceof ServerCubicWorld serverWorld) {
            return serverWorld.getServer();
        }
        return null;
    }

    @Override
    public IWorld getWorld() {
        return entity.getWorld();
    }
}
