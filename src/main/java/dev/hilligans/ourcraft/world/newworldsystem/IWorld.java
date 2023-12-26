package dev.hilligans.ourcraft.world.newworldsystem;

import dev.hilligans.ourcraft.block.blockstate.IBlockState;
import dev.hilligans.ourcraft.block.Blocks;
import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.data.other.ChunkPos;
import dev.hilligans.ourcraft.entity.Entity;
import dev.hilligans.ourcraft.util.Immutable;
import dev.hilligans.ourcraft.util.MathUtil;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.Iterator;

public interface IWorld {

    String getName();
    
    int getID();

    void tick();

    default IBlockState getBlockState(long x, long y, long z) {
        IChunk chunk = getChunkNonNull(x,y,z);
        if(chunk != null) {
            return chunk.getBlockState1(x, y, z);
        } else {
            return Blocks.AIR.getDefaultState();
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

    default boolean swapBlockState(long blockX, long blockY, long blockZ, IBlockState expected, IBlockState to) {
        IChunk chunk = getChunk(blockX, blockY, blockZ);
        if(chunk != null) {
            return chunk.swapBlockState(blockX, blockY, blockZ, expected, to);
        }
        return false;
    }

    default boolean swapBlockState(BlockPos pos, IBlockState expected, IBlockState to) {
        return swapBlockState(pos.getX(), pos.getY(), pos.getZ(), expected, to);
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

    default IBlockState getIntersection(double startX, double startY, double startZ, float dirX, float dirY, float dirZ, float length) {
        double X = startX + dirX * length;
        double Y = startY + dirY * length;
        double Z = startZ + dirZ * length;

        long minX = MathUtil.floor(Math.min(X, startX));
        long minY = MathUtil.floor(Math.min(Y, startY));
        long minZ = MathUtil.floor(Math.min(Z, startZ));

        long maxX = MathUtil.ceil(Math.max(X, startX));
        long maxY = MathUtil.ceil(Math.max(Y, startY));
        long mazZ = MathUtil.ceil(Math.max(Z, startZ));

        Vector2f vector2f = new Vector2f();

        IBlockState state = null;
        float l = length + 1;

        for(long x = minX; x < maxX; x++) {
            for(long y = minY; y < maxY; y++) {
                for(long z = minZ; z < mazZ; z++) {
                    IBlockState blockState = getBlockState(x, y, z);
                    float res = blockState.getBlock().getBoundingBox(blockState).intersectsRay((float) (x - startX), (float) (y - startY), (float) (z - startZ), dirX, dirY, dirZ, vector2f);
                    if(res != -1) {
                        if(res < l) {
                            state = blockState;
                            l = res;
                        }
                    }
                }
            }
        }
        if(l > length) {
            return null;
        }
        return state;
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
