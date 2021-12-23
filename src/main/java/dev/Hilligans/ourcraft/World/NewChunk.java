package dev.Hilligans.ourcraft.World;

import dev.Hilligans.ourcraft.Data.Other.BlockStates.BlockState;
import dev.Hilligans.ourcraft.Util.Settings;

import java.util.ArrayList;

public class NewChunk {

    public int x;
    public int y;
    public int z;

    public boolean needsSaving;
    public boolean populated = false;

    public ArrayList<SubChunk> subChunks = new ArrayList<>();
    public IWorld world;

    public NewChunk(int x, int y, int z, IWorld world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;

        for(int a = 0; a < Settings.chunkHeight; a++) {
            //SubChunk subChunk = new SubChunk(world,this.x * 16,a * 16, this.z * 16);
            //chunks.add(subChunk);
        }
    }

    public BlockState setBlockState(int x, int y, int z, BlockState blockState) {

        return null;
    }

    public SubChunk getSubChunk(int x, int y, int z) {
        return subChunks.get((y & 0b10000) >> 2 | (z & 0b10000) >> 3 | (x & 0b10000) >> 4);
    }



}
