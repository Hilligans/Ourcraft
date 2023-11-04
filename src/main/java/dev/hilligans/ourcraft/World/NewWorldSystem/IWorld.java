package dev.hilligans.ourcraft.World.NewWorldSystem;

import dev.hilligans.ourcraft.Block.BlockState.IBlockState;
import dev.hilligans.ourcraft.Block.Blocks;
import dev.hilligans.ourcraft.Data.Other.BlockPos;
import dev.hilligans.ourcraft.Data.Other.ChunkPos;
import dev.hilligans.ourcraft.Entity.Entity;
import dev.hilligans.ourcraft.Util.Immutable;
import dev.hilligans.ourcraft.Util.Math.Vector3fi;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.Iterator;
import java.util.function.Supplier;

public interface IWorld {

    String getName();
    
    int getID();

    void tick();

    default IBlockState getBlockState(long x, long y, long z) {
        IChunk chunk = getChunkNonNull(x,y,z);
        if(chunk != null) {
            return chunk.getBlockState1(x, y, z);
        } else {
            return Blocks.AIR.getDefaultState1();
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

    int getChunkWidth();
    int getChunkHeight();

    default int getSubChunkWidth() {
        return 16;
    }

    default int getSubChunkHeight() {
        return 16;
    }

    default ChunkPos getConverted(BlockPos blockPos) {
        return new ChunkPos(blockPos, getChunkWidth(), getChunkHeight());
    }

    default ChunkPos getConverted(long blockX, long blockY, long blockZ) {
        return new ChunkPos(blockX, blockY, blockZ, getChunkWidth(), getChunkHeight());
    }

    Vector3f DOWN = new Vector3f(0,-1,0);

    @Immutable
    default Vector3fc getGravityVector(Vector3f position) {
        return DOWN;
    }

    default Vector3fc getBlockGravity(int x, int y, int z) {
        return DOWN;
    }

    IThreeDChunkContainer getChunkContainer();

    void addEntity(Entity entity);

    Entity removeEntity(long l1, long l2);

    default Iterable<IChunk> getChunks(BlockPos min, BlockPos max) {
        return getChunks(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
    }

    default Iterable<IChunk> getChunks(long minX, long minY, long minZ, long maxX, long maxY, long maxZ) {
        return new Iterable<>() {
            @NotNull
            @Override
            public Iterator<IChunk> iterator() {
                return new Iterator<IChunk>() {
                    final int chunkWidth = getChunkWidth();
                    final int chunkHeight = getChunkHeight();

                    long x = minX;
                    long y = minY;
                    long z = minZ;

                    @Override
                    public boolean hasNext() {
                        if(maxZ <= z) {
                            return false;
                        }
                        IChunk chunk = getChunk(x, y, z);
                        while (chunk == null || maxX > z) {
                            inc();
                            if(maxX > z) {
                                chunk = getChunk(x, y, z);
                            }
                        }
                        return (maxZ > z);
                    }

                    @Override
                    public IChunk next() {
                        IChunk chunk = getChunk(x, y, z);
                        inc();
                        return chunk;
                    }

                    public void inc() {
                        y += chunkHeight;
                        if (y > maxY) {
                            y = minY;
                            x += chunkWidth;
                            if(x > maxX) {
                                x = minX;
                                z += chunkWidth;
                            }
                        }
                    }
                };
            }
        };
    }

}
