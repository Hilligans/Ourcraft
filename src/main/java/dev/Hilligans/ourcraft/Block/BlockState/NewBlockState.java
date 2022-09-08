package dev.Hilligans.ourcraft.Block.BlockState;

import dev.Hilligans.ourcraft.Block.Block;

import java.util.ArrayList;
import java.util.HashMap;

public class NewBlockState implements IBlockState {

    public int blockStateReferenceID;
    public short blockData;
    public Block block;

    public NewBlockState(Block block) {
        this.block = block;
    }

    public int size = 1;
    public ArrayList<IBlockStateType<?>> vals = new ArrayList<>();
    public Object[] map;


    @Override
    public Block getBlock() {
        return block;
    }

    public void register(IBlockStateType<?> type) {
        size *= type.getCount();
        vals.add(type);
    }

    public void build(int id) {
        map = new Object[vals.size()];
        int x = 0;
        for(IBlockStateType<?> stateType : vals) {
            int pos = Math.floorDiv(id, stateType.getCount());
            id = id % stateType.getCount();
            map[x++] = stateType.getValue(pos);
        }
    }

    /**
     * just using iteration due to most of the time blocks will only have a few state types and would perform better than a map.
     */
    public Object getValue(IBlockStateType<?> type) {
        for(int x = 0; x < vals.size(); x++) {
            if(vals.get(x) == type) {
                return map[x];
            }
        }
        return null;
    }

    public IBlockState getNewState(IBlockStateType<?> stateType, Object obj) {
        int id = 0;
        for(int x = vals.size() - 1 ; x == 0; x--) {
            IBlockStateType<?> type = vals.get(x);
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
}
