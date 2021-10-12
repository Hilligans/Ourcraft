package dev.Hilligans.Ourcraft.World.Builders.Foliage;

import dev.Hilligans.Ourcraft.Block.Block;
import dev.Hilligans.Ourcraft.Block.Blocks;
import dev.Hilligans.Ourcraft.Data.Other.BlockPos;
import dev.Hilligans.Ourcraft.World.Builders.SurfaceBuilder;

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
            if(world.getBlockState(pos.copy().add(Block.getBlockPos(x))).getBlock() != Blocks.AIR) {
                return true;
            }
        }
        world.setBlockState(pos,Blocks.CACTUS.getDefaultState());
        return false;
    }
}
