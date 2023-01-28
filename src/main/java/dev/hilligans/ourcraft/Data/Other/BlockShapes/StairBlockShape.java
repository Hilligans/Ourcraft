package dev.hilligans.ourcraft.Data.Other.BlockShapes;

import dev.hilligans.ourcraft.Data.Other.BlockPos;
import dev.hilligans.ourcraft.Data.Other.BoundingBox;
import dev.hilligans.ourcraft.Data.Other.JoinedBoundingBox;
import dev.hilligans.ourcraft.World.World;

public class StairBlockShape extends BlockShape {

    public StairBlockShape() {
        super("stair.txt");
    }

    @Override
    public BoundingBox getBoundingBox(World world, BlockPos pos) {
        return new JoinedBoundingBox(0,0,0,1,0.5f,1).addBox(0,0.5f,0.5f,1,1,1);
    }
}
