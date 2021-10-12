package dev.Hilligans.Ourcraft.ModHandler.Events.Common;

import dev.Hilligans.Ourcraft.Data.Other.BlockPos;
import dev.Hilligans.Ourcraft.ModHandler.Event;
import dev.Hilligans.Ourcraft.World.World;

public class BlockChangeEvent extends Event {

    public World world;
    public BlockPos pos;

    public BlockChangeEvent(World world, BlockPos pos) {
        this.world = world;
        this.pos = pos;
    }
}
