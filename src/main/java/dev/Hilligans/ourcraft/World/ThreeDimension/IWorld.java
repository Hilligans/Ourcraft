package dev.Hilligans.ourcraft.World.ThreeDimension;

import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import dev.Hilligans.ourcraft.Data.Other.BlockStates.BlockState;
import dev.Hilligans.ourcraft.Data.Other.ChunkPos;
import dev.Hilligans.ourcraft.World.Chunk;
import dev.Hilligans.ourcraft.World.NewChunk;

public interface IWorld {

    void scheduleTick(BlockPos pos, int time);

    void updateBlock(BlockPos pos);

    default BlockState setBlockState(int x, int y, int z, BlockState blockState) {
        NewChunk chunk = getChunk(x >> 4, y >> 4, z >> 4);
        BlockState b = chunk.getBlockState(x,y,z);
        chunk.setBlockState(x,y,z,blockState);
        return b;
    }

    default BlockState setBlockState(BlockPos pos, BlockState blockState) {
        return setBlockState(pos.x,pos.y,pos.z,blockState);
    }

    NewChunk getChunk(int x, int y, int z);

    default NewChunk getChunk(BlockPos pos) {
        return getChunk(pos.x,pos.y,pos.z);
    }

    void addChunk(NewChunk chunk);

    void removeChunk(ChunkPos chunkPos);

}
