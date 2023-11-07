package dev.hilligans.ourcraft.world.data.providers;

import dev.hilligans.ourcraft.world.BlockStateDataProvider;

public class ShortBlockState extends BlockStateDataProvider {

    short rot;

    public ShortBlockState(short rot) {
        this.rot = rot;
    }

    @Override
    public void read(short in) {
        rot = in;
    }

    @Override
    public short write() {
        return rot;
    }

    @Override
    public BlockStateDataProvider duplicate() {
        return new ShortBlockState(rot);
    }
}
