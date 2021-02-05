package Hilligans.World.Builders;

import Hilligans.World.BlockPos;
import Hilligans.World.World;

public abstract class WorldBuilder {

    public World world;

    public WorldBuilder(World world) {
        this.world = world;
    }


    public abstract void build(BlockPos startPos);

}
