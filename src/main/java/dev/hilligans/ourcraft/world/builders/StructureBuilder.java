package dev.hilligans.ourcraft.world.builders;

import dev.hilligans.ourcraft.data.other.BlockPos;

public abstract class StructureBuilder extends SurfaceBuilder {


    public StructureBuilder(String featureName) {
        super(featureName);
    }

    @Override
    public void build(BlockPos startPos) {

    }
}
