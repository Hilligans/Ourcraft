package dev.hilligans.ourcraft.world.builders.foliage;

import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.block.Blocks;
import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.world.builders.SurfaceBuilder;

public class CactusBuilder extends SurfaceBuilder {

    public CactusBuilder(String featureName) {
        super(featureName);
    }

    @Override
    public void build(BlockPos startPos) {
        int height = random.nextInt(4);
        if(isPlacedOn(startPos, Blocks.SAND)) {
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
