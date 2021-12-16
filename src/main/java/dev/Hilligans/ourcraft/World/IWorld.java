package dev.Hilligans.ourcraft.World;

import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import dev.Hilligans.ourcraft.Data.Other.BlockStates.BlockState;
import dev.Hilligans.ourcraft.Data.Other.ChunkPos;

public interface IWorld {

    void scheduleTick(BlockPos pos, int time);

    void updateBlock(BlockPos pos);

    BlockState setBlockState(int x, int y, int z, BlockState blockState);

    default BlockState setBlockState(BlockPos pos, BlockState blockState) {
        return setBlockState(pos.x,pos.y,pos.z,blockState);
    }

    Chunk getChunk(int x, int y, int z);

    default Chunk getChunk(BlockPos pos) {
        return getChunk(pos.x,pos.y,pos.z);
    }

    void addChunk(Chunk chunk);

    void removeChunk(ChunkPos chunkPos);

}
