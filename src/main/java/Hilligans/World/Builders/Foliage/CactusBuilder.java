package Hilligans.World.Builders.Foliage;

import Hilligans.Block.Blocks;
import Hilligans.World.BlockPos;
import Hilligans.World.Builders.SurfaceBuilder;

public class CactusBuilder extends SurfaceBuilder {

    @Override
    public void build(BlockPos startPos) {
        int height = random.nextInt(3);
        for(int x = 0; x < height; x++) {
            if(isPlacedOn(startPos,Blocks.SAND)) {
                world.setBlockState(startPos.copy().add(0, x, 0), Blocks.CACTUS.getDefaultState());
            }
        }
    }
}
