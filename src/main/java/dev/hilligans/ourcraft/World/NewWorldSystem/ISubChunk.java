package dev.hilligans.ourcraft.World.NewWorldSystem;

import dev.hilligans.ourcraft.Block.BlockState.IBlockState;
import org.jetbrains.annotations.Nullable;

public interface ISubChunk {

    int getWidth();

    int getHeight();

    IBlockState getBlockState(long x, long y, long z);

    IBlockState setBlockState(long x, long y, long z, IBlockState blockState);

    boolean isEmpty();

    default void free(@Nullable IWorld world) {}
}
