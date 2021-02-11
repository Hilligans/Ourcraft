package Hilligans.World.DataProviders;

import Hilligans.World.BlockStateDataProvider;

public class TopBottomBlockState extends BlockStateDataProvider {

    short rot;

    public TopBottomBlockState(short rot) {
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
}
