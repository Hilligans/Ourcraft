package dev.hilligans.ourcraft.world.newworldsystem;

import dev.hilligans.ourcraft.block.blockstate.IBlockState;
import org.jetbrains.annotations.Nullable;

public interface ISubChunk {

    int getWidth();

    int getHeight();

    IBlockState getBlockState(int x, int y, int z);

    IBlockState setBlockState(int x, int y, int z, IBlockState blockState);

    boolean isEmpty();

    /**
     * @param blockState the block to insert
     * @return null if can be inserted or a new subchunk if must be replaced
     */
    ISubChunk canInsertOrGetNext(IBlockState blockState);

    default void free(@Nullable IWorld world) {}
}
