package dev.hilligans.ourcraft.world.newworldsystem;

import dev.hilligans.ourcraft.block.blockstate.IBlockState;
import dev.hilligans.ourcraft.data.other.BlockPos;
import org.joml.Vector3f;

public interface IFeaturePlacerHelper {

    IWorld getParentWorld();

    BlockPos getBlockPos();

    void setState(BlockPos pos, IBlockState blockState);

    void setState(long x, long y, long z, IBlockState blockState);

    void setStateRaw(BlockPos pos, IBlockState blockState);

    void setStateRaw(long x, long y, long z, IBlockState blockState);

    IBlockState getState(BlockPos blockPos);

    IBlockState getState(long x, long y, long z);

    IBlockState getStateRaw(BlockPos blockPos);

    IBlockState getStateRaw(long x, long y, long z);

    void setPlacementPosition(BlockPos pos);

    Vector3f X = new Vector3f(1,0,0);
    Vector3f Y = new Vector3f(0,1,0);
    Vector3f Z = new Vector3f(0,0,1);

    default Vector3f getXDirection() {
        return X;
    }

    default Vector3f getYDirection() {
        return Y;
    }

    default Vector3f getZDirection() {
        return Z;
    }
}
