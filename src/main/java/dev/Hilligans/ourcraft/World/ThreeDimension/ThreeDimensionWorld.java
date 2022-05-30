package dev.Hilligans.ourcraft.World.ThreeDimension;

import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import dev.Hilligans.ourcraft.Data.Other.BlockStates.BlockState;
import dev.Hilligans.ourcraft.Data.Other.ChunkPos;
import dev.Hilligans.ourcraft.World.Chunk;
import dev.Hilligans.ourcraft.World.NewChunk;

public class ThreeDimensionWorld implements IWorld {

    IThreeDChunkContainer chunkContainer;

    @Override
    public void scheduleTick(BlockPos pos, int time) {

    }

    @Override
    public void updateBlock(BlockPos pos) {

    }
    
    @Override
    public NewChunk getChunk(int x, int y, int z) {
        return chunkContainer.getChunk(x,y,z);
    }

    @Override
    public void addChunk(NewChunk chunk) {
        chunkContainer.setChunk(chunk.x,chunk.y,chunk.z,chunk);
    }

    @Override
    public void removeChunk(ChunkPos chunkPos) {

    }
}
