package Hilligans.Data.Other.BlockShapes;

import Hilligans.Data.Other.*;
import Hilligans.World.World;

public class StairBlockShape extends BlockShape {

    public StairBlockShape() {
        super("stair.txt");
    }

    @Override
    public BoundingBox getBoundingBox(World world, BlockPos pos) {
        return new JoinedBoundingBox(0,0,0,1,0.5f,1).addBox(0,0.5f,0.5f,1,1,1);
    }
}
