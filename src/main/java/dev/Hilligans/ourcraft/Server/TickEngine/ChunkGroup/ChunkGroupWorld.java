package dev.Hilligans.ourcraft.Server.TickEngine.ChunkGroup;

import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import dev.Hilligans.ourcraft.Data.Other.BlockStates.BlockState;
import dev.Hilligans.ourcraft.Data.Other.ChunkPos;
import dev.Hilligans.ourcraft.World.Chunk;
import dev.Hilligans.ourcraft.World.IWorld;

import java.util.ArrayList;

public class ChunkGroupWorld implements IWorld {

    public ArrayList<ChunkGroup> chunkGroups = new ArrayList<>();


    @Override
    public void scheduleTick(BlockPos pos, int time) {

    }

    @Override
    public void updateBlock(BlockPos pos) {

    }

    @Override
    public BlockState setBlockState(int x, int y, int z, BlockState blockState) {
        return null;
    }

    @Override
    public Chunk getChunk(int x, int y, int z) {
        return null;
    }

    @Override
    public void addChunk(Chunk chunk) {

    }

    @Override
    public void removeChunk(ChunkPos chunkPos) {

    }
}
