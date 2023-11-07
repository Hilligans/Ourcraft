package dev.hilligans.ourcraft.mod.handler.events.common;

import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.mod.handler.Event;
import dev.hilligans.ourcraft.world.World;

public class BlockChangeEvent extends Event {

    public World world;
    public BlockPos pos;

    public BlockChangeEvent(World world, BlockPos pos) {
        this.world = world;
        this.pos = pos;
    }
}
