package dev.Hilligans.Ourcraft.Data.Other.BlockShapes;

import dev.Hilligans.Ourcraft.Data.Other.BlockPos;
import dev.Hilligans.Ourcraft.Data.Other.BoundingBox;
import dev.Hilligans.Ourcraft.Data.Other.JoinedBoundingBox;
import dev.Hilligans.Ourcraft.World.World;

public class StairBlockShape extends BlockShape {

    public StairBlockShape() {
        super("stair.txt");
    }

    @Override
    public BoundingBox getBoundingBox(World world, BlockPos pos) {
        return new JoinedBoundingBox(0,0,0,1,0.5f,1).addBox(0,0.5f,0.5f,1,1,1);
    }
}
