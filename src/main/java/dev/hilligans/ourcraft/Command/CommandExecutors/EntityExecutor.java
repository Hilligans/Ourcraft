package dev.hilligans.ourcraft.Command.CommandExecutors;

import dev.hilligans.ourcraft.Entity.Entity;
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
        World world = entity.world;
        if(world instanceof ServerWorld) {
            return ((ServerWorld) world).server;
        }
        return null;
    }

    @Override
    public World getWorld() {
        return entity.getWorld();
    }
}
