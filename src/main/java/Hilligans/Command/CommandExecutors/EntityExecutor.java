package Hilligans.Command.CommandExecutors;

import Hilligans.Entity.Entity;
import Hilligans.Server.IServer;
import Hilligans.Server.MultiPlayerServer;
import Hilligans.World.ServerWorld;
import Hilligans.World.World;

public class EntityExecutor implements CommandExecutor {

    public Entity entity;

    public EntityExecutor(Entity entity) {
        this.entity = entity;
    }

    @Override
    public double getX() {
        return entity.x;
    }

    @Override
    public double getY() {
        return entity.y;
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
