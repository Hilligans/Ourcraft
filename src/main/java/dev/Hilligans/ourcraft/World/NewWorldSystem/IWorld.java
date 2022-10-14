package dev.Hilligans.ourcraft.World.NewWorldSystem;

import dev.Hilligans.ourcraft.Block.BlockState.IBlockState;
import dev.Hilligans.ourcraft.Block.Blocks;
import dev.Hilligans.ourcraft.Data.Other.BlockPos;

public interface IWorld {

    String getName();
    
    int getID();

    void tick();

    default IBlockState getBlockState(long x, long y, long z) {
        IChunk chunk = getChunkNonNull(x,y,z);
        if(chunk != null) {
            return chunk.getBlockState1(x, y, z);
        } else {
            return Blocks.STONE.getDefaultState1();
        }
    }

    default IBlockState getBlockState(BlockPos pos) {
        return getBlockState(pos.getX(),pos.getY(),pos.getZ());
    }

    default void setBlockState(long x, long y, long z, IBlockState newState) {
        IChunk chunk = getChunkNonNull(x,y,z);
        chunk.setBlockState(x,y,z,newState);
    }

    default void setBlockState(BlockPos pos, IBlockState newState) {
        setBlockState(pos.getX(),pos.getY(),pos.getZ(),newState);
    }

    IChunk getChunk(long blockX, long blockY, long blockZ);

    IChunk getChunkNonNull(long blockX, long blockY, long blockZ);

    void setChunk(long blockX, long blockY, long blockZ, IChunk chunk);

    IThreeDChunkContainer getChunkContainer();
}
