package Hilligans.World.Builders.Foliage;

import Hilligans.Block.Block;
import Hilligans.Block.Blocks;
import Hilligans.Data.Other.BlockPos;
import Hilligans.World.Builders.SurfaceBuilder;

public class CactusBuilder extends SurfaceBuilder {

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
            if(world.getBlockState(pos.copy().add(Block.getBlockPos(x))).block != Blocks.AIR) {
                return true;
            }
        }
        world.setBlockState(pos,Blocks.CACTUS.getDefaultState());
        return false;
    }
}
