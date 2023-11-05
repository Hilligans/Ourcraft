package dev.hilligans.ourcraft.Block.BlockTypes;

import dev.hilligans.ourcraft.Block.Blocks;
import dev.hilligans.ourcraft.Client.Rendering.NewRenderer.PrimitiveBuilder;
import dev.hilligans.ourcraft.Data.Other.BlockPos;
import dev.hilligans.ourcraft.Data.Other.BlockProperties;
import dev.hilligans.ourcraft.Data.Other.BlockStates.BlockState;
import dev.hilligans.ourcraft.World.World;

import java.util.Random;

public class WeepingVineBlock extends PlantBlock {
    public WeepingVineBlock(String name, BlockProperties blockProperties) {
        super(name, blockProperties);
    }

    @Override
    public void addVertices(PrimitiveBuilder primitiveBuilder, int side, float size, BlockState blockState, BlockPos blockPos, int x, int z) {
        long seed = ((long)blockPos.x) | ((long)blockPos.z << 32);
        Random random = new Random(seed);
        for(int a = 0; a < 3; a++) {
            blockProperties.blockShape.addVertices(primitiveBuilder,side,size,blockState,blockProperties.blockTextureManager, new BlockPos(x,blockPos.y,z).get3f(),random.nextFloat() - 0.5f, 0,random.nextFloat() - 0.5f);
        }
    }

   // @Override
   // public void randomTick(World world, BlockPos pos) {
   //     if(world.getBlockState(pos.add(0,1,0)).getBlock() != Blocks.WEEPING_VINE) {
   //         world.setBlockState(pos, Blocks.WEEPING_VINE.getDefaultState());
   //     }
   // }


}
