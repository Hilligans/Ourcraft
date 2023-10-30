package dev.hilligans.ourcraft.Command.CommandExecutors;

import dev.hilligans.ourcraft.Entity.Entity;
import dev.hilligans.ourcraft.World.NewWorldSystem.IWorld;
import dev.hilligans.ourcraft.World.ServerWorld;
import dev.hilligans.ourcraft.World.World;
import dev.hilligans.ourcraft.Server.IServer;

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
        if(world instanceof ServerWorld serverWorld) {
            return serverWorld.server;
        }
        return null;
    }

    @Override
    public IWorld getWorld() {
        return entity.getWorld();
    }
}
