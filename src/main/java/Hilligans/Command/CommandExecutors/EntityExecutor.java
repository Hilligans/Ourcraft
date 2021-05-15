package Hilligans.Command.CommandExecutors;

import Hilligans.Entity.Entity;
import Hilligans.World.World;

public class EntityExecutor implements CommandExecutor {

    Entity entity;

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
    public World getWorld() {
        return entity.getWorld();
    }
}
