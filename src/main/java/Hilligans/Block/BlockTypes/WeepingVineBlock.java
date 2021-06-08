package Hilligans.Block.BlockTypes;

import Hilligans.Client.Rendering.NewRenderer.PrimitiveBuilder;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Data.Other.BlockProperties;
import Hilligans.Data.Other.BlockState;

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


}
