package Hilligans.Data.Other;

import Hilligans.Block.Block;
import Hilligans.World.BlockStateDataProvider;

public class DataBlockState extends BlockState {

    public BlockStateDataProvider blockData;

    public DataBlockState(Block block) {
        super(block);
    }

    public DataBlockState(Block block, BlockStateDataProvider blockStateDataProvider) {
        super(block);
        blockData = blockStateDataProvider;
    }

    public short readData() {
        if(blockData == null) {
            return 0;
        }
        return blockData.write();
    }

    public BlockState write(short in) {
        if(blockData != null) {
            blockData.read(in);
        }
        return this;
    }
}
