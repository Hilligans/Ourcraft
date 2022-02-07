package dev.Hilligans.ourcraft.World.Builders.Foliage;

import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Block.Blocks;
import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import dev.Hilligans.ourcraft.World.Builders.SurfaceBuilder;

public class CactusBuilder extends SurfaceBuilder {

    public CactusBuilder(String featureName) {
        super(featureName);
    }

    @Override
    public void build(BlockPos startPos) {
        int height = random.nextInt(4);
        if(isPlacedOn(startPos,Blocks.SAND)) {
            for (int x = 0; x < height; x++) {
                if (tryPlace(startPos.copy().add(0, x, 0))) {
                    break;
                }
            }
        }
    }

    public boolean tryPlace(BlockPos pos) {
        for(int x = 0; x < 4; x++) {
            if(world.getBlockState(pos.copy().add(Block.getBlockPos(x))).getBlock() != Blocks.AIR) {
                return true;
            }
        }
        world.setBlockState(pos,Blocks.CACTUS.getDefaultState());
        return false;
    }
}
