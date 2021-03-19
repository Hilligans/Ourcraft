package Hilligans.Block.BlockTypes;

import Hilligans.Block.Block;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Data.Other.BlockProperties;
import Hilligans.Data.Other.BlockState;
import Hilligans.Data.Other.DataBlockState;
import Hilligans.Util.Vector5f;
import Hilligans.World.DataProviders.ShortBlockState;

import org.joml.Vector3f;

import java.util.Random;

public class ColorBlock extends Block {
    public ColorBlock(String name, BlockProperties blockProperties) {
        super(name, blockProperties);
    }

    @Override
    public boolean hasBlockState() {
        return true;
    }

    @Override
    public int blockStateByteCount() {
        return 2;
    }

    @Override
    public Vector5f[] getVertices(int side, float size, BlockState blockState, BlockPos blockPos) {
        Vector5f[] vector5fs = super.getVertices(side, size, blockState, blockPos);
        float r = 32f / (((DataBlockState)blockState).readData() & 31);
        float g = 32f / (((DataBlockState)blockState).readData() >> 5 & 31);
        float b = 32f / (((DataBlockState)blockState).readData() >> 10 & 31);

        for(Vector5f vector5f : vector5fs) {
            vector5f.setColored(r,g,b,1.0f);
        }
        return vector5fs;
    }

    @Override
    public Vector5f[] getVertices(int side, BlockState blockState, BlockPos blockPos) {
        Vector5f[] vector5fs = super.getVertices(side, blockState, blockPos);
        float r = 32f / (((DataBlockState)blockState).readData() & 31);
        float g = 32f / (((DataBlockState)blockState).readData() >> 5 & 31);
        float b = 32f / (((DataBlockState)blockState).readData() >> 10 & 31);

        for(Vector5f vector5f : vector5fs) {
            vector5f.setColored(r,g,b,1.0f);
        }
        return vector5fs;
    }

    @Override
    public BlockState getDefaultState() {
        return new DataBlockState(this, new ShortBlockState((short) new Random().nextInt()));
    }

    @Override
    public BlockState getStateWithData(short data) {
        return new DataBlockState(this, new ShortBlockState(data));
    }
}
