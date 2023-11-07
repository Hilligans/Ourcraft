package dev.hilligans.ourcraft.block.blockstate;

public class IntegerStateType implements IBlockStateType<Integer> {

    public String name;
    public int count;
    public int defaultValue = 0;

    public IntegerStateType(String name, int count) {
        this.name = name;
        this.count = count;
    }

    @Override
    public IBlockStateType<Integer> setDefaultValue(Integer val) {
        defaultValue = val;
        return this;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Integer getDefaultValue() {
        return defaultValue;
    }

    @Override
    public Integer getValue(int index) {
        return index;
    }

    @Override
    public int getIndexCast(Integer value) {
        return value;
    }
}
