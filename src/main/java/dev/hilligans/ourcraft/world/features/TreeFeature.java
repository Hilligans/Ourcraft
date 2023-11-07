package dev.hilligans.ourcraft.world.features;

import dev.hilligans.ourcraft.block.Blocks;
import dev.hilligans.ourcraft.world.newworldsystem.IFeature;
import dev.hilligans.ourcraft.world.newworldsystem.IFeaturePlacerHelper;

public class TreeFeature implements IFeature {

    @Override
    public void place(IFeaturePlacerHelper placerHelper) {
        if(placerHelper.getState(1,0,0).getBlock().blockProperties.airBlock && placerHelper.getState(-1,0,0).getBlock().blockProperties.airBlock && placerHelper.getState(0,0,1).getBlock().blockProperties.airBlock && placerHelper.getState(0,0,-1).getBlock().blockProperties.airBlock) {
            for (int y = 0; y < 8; y++) {
                if (y != 6 && y != 7) {
                    placerHelper.setState(0, y, 0, Blocks.LOG.getDefaultState1());
                }
                if (y >= 5) {
                    for (int x = -2; x < 3; x++) {
                        for (int z = -2; z < 3; z++) {
                            if ((x != 0 && z != 0) || y >= 6) {
                                placerHelper.setState(x, y, z, Blocks.LEAVES.getDefaultState1());
                            }
                        }
                    }
                }
            }
        }
    }
}
