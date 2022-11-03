package dev.Hilligans.ourcraft.World.NewWorldSystem;

import dev.Hilligans.ourcraft.Block.BlockState.IBlockState;
import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import dev.Hilligans.ourcraft.Data.Other.BlockStates.BlockState;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface IChunk {

    int getWidth();
    int getHeight();

    long getX();
    long getY();
    long getZ();

    default long getBlockX() {
        return getX() * getWidth();
    }

    default long getBlockY() {
        return getY() * getHeight();
    }

    default long getBlockZ() {
        return getZ() * getWidth();
    }

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

    default IBlockState getBlockState1(BlockPos pos) {
        return getBlockState1(pos.getX(),pos.getY(),pos.getZ());
    }

    void setBlockState(long x, long y, long z, IBlockState blockState);

    void setChunkPosition(long x, long y, long z);

    IWorld getWorld();

    boolean isEmpty();

    /**
     * Spec does not forbid subchunk and chunk from being the same object.
     * This method can run on the chunk itself acting like a single subchunk.
     */

    void forEach(Consumer<ISubChunk> consumer);

    void setDirty(boolean value);

    boolean isDirty();

    boolean isGenerated();

    boolean isPopulated();

    boolean isStructured();

    default void fill(IBlockState state, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        for(int x = minX; x < maxX; x++) {
            for(int y = minY; y < maxY; y++) {
                for(int z = minZ; z < maxZ; z++) {
                    setBlockState(x,y,z,state);
                }
            }
        }
    }

    default void free(@Nullable IWorld world) {}

    default void set(@NotNull IWorld world) {}
}
