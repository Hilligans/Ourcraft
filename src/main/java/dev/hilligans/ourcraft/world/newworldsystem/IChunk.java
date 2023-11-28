package dev.hilligans.ourcraft.world.newworldsystem;

import dev.hilligans.ourcraft.block.blockstate.IBlockState;
import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.data.other.ChunkPos;
import dev.hilligans.ourcraft.data.other.IBoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Intersectionf;
import org.joml.Vector2f;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public interface IChunk extends IBoundingBox {

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

    default long getBlockMaxX() {
        return getBlockX() + getWidth() - 1;
    }

    default long getBlockMaxY() {
        return getBlockY() + getHeight() - 1;
    }

    default long getBlockMaxZ() {
        return getBlockZ() + getWidth() - 1;
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

    ISubChunk get(long blockX, long blockY, long blockZ);

    default ChunkPos getChunkPos() {
        return new ChunkPos(getX(), getY(), getZ());
    }

    IBlockState getBlockState1(long x, long y, long z);

    default IBlockState getBlockState1(BlockPos pos) {
        return getBlockState1(pos.getX(),pos.getY(),pos.getZ());
    }

    void setBlockState(long x, long y, long z, IBlockState blockState);

    void setChunkPosition(long x, long y, long z);

    IWorld getWorld();

    IChunk setWorld(IWorld world);

    boolean isEmpty();

    /**
     * Spec does not forbid subchunk and chunk from being the same object.
     * This method can run on the chunk itself acting like a single subchunk.
     */

    int getSubChunkCount();

    void forEach(Consumer<ISubChunk> consumer);

    void replace(UnaryOperator<ISubChunk> replacer);

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

    @Override
    default float minX() {
        return getX() * getWidth();
    }

    @Override
    default float minY() {
        return getY() * getHeight();
    }

    @Override
    default float minZ() {
        return getZ() * getWidth();
    }

    @Override
    default float maxX() {
        return (getX() + 1) * getWidth();
    }

    @Override
    default float maxY() {
        return (getY() + 1) * getHeight();
    }

    @Override
    default float maxZ() {
        return (getZ() + 1) * getWidth();
    }

    @Override
    default float intersectsRay(float x, float y, float z, float dirX, float dirY, float dirZ, Vector2f vector2f) {
        if (!Intersectionf.intersectRayAab(x, y, z, dirX, dirY, dirZ, minX(), minY(), minZ(), maxX(), maxY(), maxZ(), vector2f)) {
            return -1;
        }
        return vector2f.x;
    }
}
