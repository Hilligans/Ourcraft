package dev.hilligans.ourcraft.World.Builders;

import dev.hilligans.ourcraft.Data.Other.BlockPos;

public abstract class StructureBuilder extends SurfaceBuilder {


    public StructureBuilder(String featureName) {
        super(featureName);
    }

    @Override
    public void build(BlockPos startPos) {

    }
}
