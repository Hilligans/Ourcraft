package dev.Hilligans.ModHandler.Events.Common;

import dev.Hilligans.Data.Other.BlockPos;
import dev.Hilligans.ModHandler.Event;
import dev.Hilligans.World.World;

public class BlockChangeEvent extends Event {

    public World world;
    public BlockPos pos;

    public BlockChangeEvent(World world, BlockPos pos) {
        this.world = world;
        this.pos = pos;
    }
}
