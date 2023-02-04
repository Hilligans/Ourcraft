package dev.hilligans.planets.world;

import dev.hilligans.ourcraft.Block.BlockState.IBlockState;
import dev.hilligans.ourcraft.Data.Other.BlockPos;
import dev.hilligans.ourcraft.World.NewWorldSystem.IFeaturePlacerHelper;
import dev.hilligans.ourcraft.World.NewWorldSystem.IWorld;
import org.joml.*;

import java.lang.Math;
import java.util.Arrays;

public class PlanetFeaturePlacerHelper implements IFeaturePlacerHelper {

    public int worldRadius;
    public IWorld world;
    public BlockPos pos;
    public Matrix3d transformMatrix;

    public PlanetFeaturePlacerHelper(IWorld world) {
        this(world, new BlockPos(0, 0, 0));
    }

    public PlanetFeaturePlacerHelper(IWorld world, BlockPos position) {
        this.world = world;
        this.pos = position;
        this.transformMatrix = new Matrix3d();
        updateTransform();
    }

    public Vector3f getGravityVector(Vector3f position) {
        int component = position.maxComponent();
        return new Vector3f().setComponent(component, position.get(component) > 0 ? -1 : 1);
    }

    public void updateTransform() {
        Vector3f vector3f = pos.get3f();
        int component = vector3f.maxComponent() + 1;
        int sign = vector3f.get(component - 1) > 0 ? 1 : -1;
        System.err.println((sign * component) + 3);
        transformMatrix = table[(sign * component) + 3];
    }

    @Override
    public IWorld getParentWorld() {
        return world;
    }

    @Override
    public BlockPos getBlockPos() {
        return pos;
    }

    @Override
    public void setState(BlockPos pos, IBlockState blockState) {
        Vector3d vector3d = pos.get3d().mul(transformMatrix);
        world.setBlockState((long) vector3d.x + this.pos.x, (long) vector3d.y + this.pos.y, (long) vector3d.z + this.pos.z, blockState);
    }

    @Override
    public void setState(long x, long y, long z, IBlockState blockState) {
        Vector3d vector3d = new Vector3d(x, y, z).mul(transformMatrix);
        world.setBlockState((long) vector3d.x + pos.x, (long) vector3d.y + pos.y, (long) vector3d.z + pos.z, blockState);
    }

    @Override
    public void setStateRaw(BlockPos pos, IBlockState blockState) {
        world.setBlockState(pos, blockState);
    }

    @Override
    public void setStateRaw(long x, long y, long z, IBlockState blockState) {
        world.setBlockState(x, y, z, blockState);
    }

    @Override
    public IBlockState getState(BlockPos blockPos) {
        Vector3d vector3d = blockPos.get3d().mul(transformMatrix);
        return world.getBlockState((long) vector3d.x + pos.x, (long) vector3d.y + pos.y, (long) vector3d.z + pos.z);
    }

    @Override
    public IBlockState getState(long x, long y, long z) {
        Vector3d vector3d = new Vector3d(x, y, z).mul(transformMatrix);
        return world.getBlockState((long) vector3d.x + pos.x, (long) vector3d.y + pos.y, (long) vector3d.z + pos.z);
    }

    @Override
    public IBlockState getStateRaw(BlockPos blockPos) {
        return world.getBlockState(blockPos);
    }

    @Override
    public IBlockState getStateRaw(long x, long y, long z) {
        return world.getBlockState(x, y, z);
    }

    @Override
    public void setPlacementPosition(BlockPos pos) {
        this.pos.set(pos);
        updateTransform();
    }

    @Override
    public Vector3f getXDirection() {
        return new Vector3f(1, 0, 0).mul(transformMatrix);
    }

    @Override
    public Vector3f getYDirection() {
        return new Vector3f(0, 1, 0).mul(transformMatrix);
    }

    @Override
    public Vector3f getZDirection() {
        return new Vector3f(0, 0, 1).mul(transformMatrix);
    }

    public static final Matrix3d[] table = {
            new Matrix3d().rotateLocalX(-Math.PI/2),
            new Matrix3d().rotateLocalZ(Math.PI),
            new Matrix3d().rotateLocalZ(Math.PI/2),
            null,
            new Matrix3d().rotateLocalZ(-Math.PI/2),
            new Matrix3d(),
            new Matrix3d().rotateLocalX(Math.PI/2)
    };
}
