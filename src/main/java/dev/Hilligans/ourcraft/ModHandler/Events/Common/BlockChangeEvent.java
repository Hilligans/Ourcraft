package dev.Hilligans.ourcraft.ModHandler.Events.Common;

import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import dev.Hilligans.ourcraft.ModHandler.Event;
import dev.Hilligans.ourcraft.World.World;

public class BlockChangeEvent extends Event {

    public World world;
    public BlockPos pos;

    public BlockChangeEvent(World world, BlockPos pos) {
        this.world = world;
        this.pos = pos;
    }
}
