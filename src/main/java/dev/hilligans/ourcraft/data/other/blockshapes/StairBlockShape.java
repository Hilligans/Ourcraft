package dev.hilligans.ourcraft.data.other.blockshapes;

import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.data.other.BoundingBox;
import dev.hilligans.ourcraft.data.other.JoinedBoundingBox;
import dev.hilligans.ourcraft.world.World;

public class StairBlockShape extends BlockShape {

    public StairBlockShape() {
        super("stair.txt");
    }

    @Override
    public BoundingBox getBoundingBox(World world, BlockPos pos) {
        return new JoinedBoundingBox(0,0,0,1,0.5f,1).addBox(0,0.5f,0.5f,1,1,1);
    }
}
