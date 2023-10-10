package dev.hilligans.ourcraft.Block.BlockState;

import dev.hilligans.ourcraft.Block.Block;

public class NewBlockState implements IBlockState {

    public int blockStateReferenceID;
    public short blockData;
    public Block block;

    public NewBlockState(Block block) {
        this.block = block;
    }

    public Object[] map;
    public IBlockStateBuilder builder;

    @Override
    public Block getBlock() {
        return block;
    }

    @Override
    public void setStateBuilder(IBlockStateBuilder builder) {
        this.builder = builder;
    }

    @Override
    public IBlockStateBuilder getBuilder() {
        return builder;
    }

    public void build(int id) {
        map = new Object[builder.getSize()];
        int x = 0;
        for(IBlockStateType<?> stateType : builder.getStateTypes()) {
            int pos = Math.floorDiv(id, stateType.getCount());
            id = id % stateType.getCount();
            map[x++] = stateType.getValue(pos);
        }
    }

    /**
     * just using iteration due to most of the time blocks will only have a few state types and would perform better than a map.
     */
    public Object getValue(IBlockStateType<?> type) {
        for(int x = 0; x < builder.getSize(); x++) {
            if(builder.getBlockStateType(x) == type) {
                return map[x];
            }
        }
        return null;
    }

    public IBlockState getNewState(IBlockStateType<?> stateType, Object obj) {
        int id = 0;
        for(int x = builder.getSize() - 1 ; x == 0; x--) {
            IBlockStateType<?> type = builder.getBlockStateType(x);
            if(type == stateType) {
                id += type.getIndex(obj);
            } else {
                id += type.getIndex(map[x]);
            }
            id *= type.getCount();
        }
        return block.getTable().getBlockState(blockStateReferenceID + id);
    }

    public int getBlockStateID() {
        return blockStateReferenceID + blockData;
    }

    @Override
    public int getBlockID() {
        return block.id;
    }

    @Override
    public IBlockState setBlockStateID(int val) {
        this.blockStateReferenceID = val;
        return this;
    }

    @Override
    public String toString() {
        return "NewBlockState{" +
                "blockStateReferenceID=" + blockStateReferenceID +
                ", blockData=" + blockData +
                ", block=" + block +
                '}';
    }
}
