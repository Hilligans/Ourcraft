package dev.Hilligans.ourcraft.World.NewWorldSystem;

import dev.Hilligans.ourcraft.Block.BlockState.IBlockState;
import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import dev.Hilligans.ourcraft.Data.Other.BlockStates.BlockState;

import java.util.function.Consumer;

public interface IChunk {

    int getWidth();
    int getHeight();

    long getX();
    long getY();
    long getZ();

    default int getYOffset() {
        return 0;
    }

    default long getChunkXBlockPos() {
        return getX() * getWidth();
    }

    default long getChunkYBlockPos() {
        return getY() * getHeight() + getYOffset();
    }

    default long getChunkZBlockPos() {
        return getZ() * getWidth();
    }

    default BlockPos getChunkBlockPos(BlockPos dest) {
        return dest.set((int)getChunkXBlockPos(),(int)getChunkYBlockPos(),(int)getChunkZBlockPos());
    }

    IBlockState getBlockState1(long x, long y, long z);
    void setBlockState(long x, long y, long z, IBlockState blockState);

    boolean isEmpty();

    /**
     * Spec does not forbid subchunk and chunk from being the same object.
     * This method can run on the chunk itself acting like a single subchunk.
     */

    void forEach(Consumer<ISubChunk> consumer);

    boolean isGenerated();

    boolean isPopulated();

    boolean isStructured();
}
