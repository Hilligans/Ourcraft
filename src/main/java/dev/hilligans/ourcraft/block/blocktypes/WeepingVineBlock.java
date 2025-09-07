package dev.hilligans.ourcraft.block.blocktypes;

import dev.hilligans.ourcraft.data.other.BlockProperties;

public class WeepingVineBlock extends PlantBlock {
    public WeepingVineBlock(String name, BlockProperties blockProperties) {
        super(name, blockProperties);
    }

    //@Override
    /*  public void addVertices(TextAtlas textAtlas, PrimitiveBuilder primitiveBuilder, int side, float size, BlockState blockState, BlockPos blockPos, int x, int z) {
        long seed = ((long)blockPos.x) | ((long)blockPos.z << 32);
        Random random = new Random(seed);
        for(int a = 0; a < 3; a++) {
            // blockProperties.blockShape.addVertices(textAtlas, primitiveBuilder,side,size,blockState,blockProperties.blockTextureManager, new BlockPos(x,blockPos.y,z).get3f(),random.nextFloat() - 0.5f, 0,random.nextFloat() - 0.5f);
        }
    }
     */

   // @Override
   // public void randomTick(World world, BlockPos pos) {
   //     if(world.getBlockState(pos.add(0,1,0)).getBlock() != Blocks.WEEPING_VINE) {
   //         world.setBlockState(pos, Blocks.WEEPING_VINE.getDefaultState());
   //     }
   // }


}
