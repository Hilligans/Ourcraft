package Hilligans.Block.BlockTypes;

import Hilligans.Block.Block;
import Hilligans.Data.Other.*;
import Hilligans.Data.Other.BlockShapes.StairBlockShape;
import Hilligans.World.BlockStateDataProvider;
import Hilligans.World.DataProviders.ShortBlockState;
import Hilligans.World.World;

public class StairBlock extends Block {
    public StairBlock(String name) {
        super(name);
        blockShape = new StairBlockShape();
        transparentTexture = true;
    }


    @Override
    public boolean hasBlockState() {
        return true;
    }

    @Override
    public BlockState getDefaultState() {
        return new DataBlockState(this, new ShortBlockState((short)0));
    }
}
