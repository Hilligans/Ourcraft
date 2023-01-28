package dev.hilligans.ourcraft.World.NewWorldSystem;

import dev.hilligans.ourcraft.Block.BlockState.IBlockState;
import dev.hilligans.ourcraft.Block.Blocks;
import dev.hilligans.ourcraft.Data.Other.BlockPos;
import dev.hilligans.ourcraft.Util.Immutable;
import dev.hilligans.ourcraft.Util.Math.Vector3fi;
import org.joml.Vector3f;
import org.joml.Vector3fc;

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

    Vector3fi DOWN = new Vector3fi(0,-1,0);

    @Immutable
    default Vector3fc getGravityVector(Vector3f position) {
        return DOWN;
    }

    default Vector3fc getBlockGravity(int x, int y, int z) {
        return DOWN;
    }

    IThreeDChunkContainer getChunkContainer();


}
