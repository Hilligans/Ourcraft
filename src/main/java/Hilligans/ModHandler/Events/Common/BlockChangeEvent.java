package Hilligans.ModHandler.Events.Common;

import Hilligans.Data.Other.BlockPos;
import Hilligans.ModHandler.Event;
import Hilligans.World.World;

public class BlockChangeEvent extends Event {

    public World world;
    public BlockPos pos;

    public BlockChangeEvent(World world, BlockPos pos) {
        this.world = world;
        this.pos = pos;
    }
}
