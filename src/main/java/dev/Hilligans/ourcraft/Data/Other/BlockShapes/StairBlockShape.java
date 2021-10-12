package dev.Hilligans.ourcraft.Data.Other.BlockShapes;

import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import dev.Hilligans.ourcraft.Data.Other.BoundingBox;
import dev.Hilligans.ourcraft.Data.Other.JoinedBoundingBox;
import dev.Hilligans.ourcraft.World.World;

public class StairBlockShape extends BlockShape {

    public StairBlockShape() {
        super("stair.txt");
    }

    @Override
    public BoundingBox getBoundingBox(World world, BlockPos pos) {
        return new JoinedBoundingBox(0,0,0,1,0.5f,1).addBox(0,0.5f,0.5f,1,1,1);
    }
}
