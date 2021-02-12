package Hilligans.Block;

import Hilligans.Block.Block;
import Hilligans.Data.Other.BoundingBox;
import Hilligans.World.BlockStateDataProvider;
import Hilligans.World.DataProvider;

public class BlockState {

    public Block block;

    public BlockStateDataProvider blockData;

    public BlockState(Block block) {
        this.block = block;
    }

    public BlockState(Block block, BlockStateDataProvider blockStateDataProvider) {
        this.block = block;
        this.blockData = blockStateDataProvider;
    }

    public BoundingBox getBoundingBox() {
        return block.getBoundingBox(this);
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
