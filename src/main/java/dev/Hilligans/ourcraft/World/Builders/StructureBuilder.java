package dev.Hilligans.ourcraft.World.Builders;

import dev.Hilligans.ourcraft.Data.Other.BlockPos;

public abstract class StructureBuilder extends SurfaceBuilder {


    public StructureBuilder(String featureName) {
        super(featureName);
    }

    @Override
    public void build(BlockPos startPos) {

    }
}
