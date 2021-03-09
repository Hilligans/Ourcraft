package Hilligans.Block.BlockTypes;

import Hilligans.Data.Other.BlockPos;
import Hilligans.Data.Other.BlockShapes.XBlockShape;
import Hilligans.Data.Other.BlockState;
import Hilligans.Util.Vector5f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class WeepingVineBlock extends PlantBlock {
    public WeepingVineBlock(String name) {
        super(name);
    }

    @Override
    public Vector5f[] getVertices(int side, float size, BlockState blockState, BlockPos blockPos) {
        return super.getVertices(side, size, blockState, blockPos);
    }

    @Override
    public Vector5f[] getVertices(int side, BlockState blockState, BlockPos blockPos) {
        ArrayList<Vector5f> vector5fs = new ArrayList<>();
        XBlockShape blockShape = (XBlockShape)this.blockShape;
        long seed = ((long)blockPos.x) | ((long)blockPos.z << 32);
        Random random = new Random(seed);
        for(int x = 0; x < 7; x++) {
            vector5fs.addAll(Arrays.asList(blockShape.getVertices(side, 0.5f, random.nextFloat() - 0.5f, random.nextFloat() - 0.5f, blockState, blockTextureManager)));
        }

        Vector5f[] vector5fs1 = new Vector5f[vector5fs.size()];
        return vector5fs.toArray(vector5fs1);
    }

    @Override
    public Integer[] getIndices(int side, int spot) {
        ArrayList<Integer> indices = new ArrayList<>();
        for(int x = 0; x < 7; x++) {
            indices.addAll(Arrays.asList(blockShape.getIndices(side,spot + x * 4)));
        }
        Integer[] integers = new Integer[indices.size()];
        return indices.toArray(integers);
    }
}
