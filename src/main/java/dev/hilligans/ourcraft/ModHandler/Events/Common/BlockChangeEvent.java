package dev.hilligans.ourcraft.ModHandler.Events.Common;

import dev.hilligans.ourcraft.Data.Other.BlockPos;
import dev.hilligans.ourcraft.ModHandler.Event;
import dev.hilligans.ourcraft.World.World;

public class BlockChangeEvent extends Event {

    public World world;
    public BlockPos pos;

    public BlockChangeEvent(World world, BlockPos pos) {
        this.world = world;
        this.pos = pos;
    }
}
