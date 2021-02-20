package Hilligans.World.DataProviders;

import Hilligans.World.BlockStateDataProvider;

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
