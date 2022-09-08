package dev.Hilligans.ourcraft.World.NewWorldSystem;

import dev.Hilligans.ourcraft.Block.BlockState.IBlockState;
import dev.Hilligans.ourcraft.Block.Blocks;
import dev.Hilligans.ourcraft.Data.Other.BlockStates.BlockState;
import dev.Hilligans.ourcraft.World.NewWorldSystem.IChunk;
import dev.Hilligans.ourcraft.World.NewWorldSystem.ISubChunk;
import dev.Hilligans.ourcraft.World.SubChunk;
import dev.Hilligans.ourcraft.World.World;

import java.util.function.Consumer;

public class ClassicChunk implements IChunk {

    public int x;
    public int z;
    public int height;

    public SubChunk[] chunks;
    public World world;

    public boolean generated = false;
    public boolean populated = false;
    public boolean structured = false;

    public ClassicChunk(World world, int height, int x, int z) {
        this.world = world;
        this.height = height;
        this.x = x;
        this.z = z;
        chunks = new SubChunk[height];
    }

    @Override
    public int getWidth() {
        return 16;
    }

    @Override
    public int getHeight() {
        return height * 16;
    }

    @Override
    public long getX() {
        return x;
    }

    @Override
    public long getY() {
        return 0;
    }

    @Override
    public long getZ() {
        return z;
    }

    @Override
    public IBlockState getBlockState1(long x, long y, long z) {
        return null;
    }

    public BlockState getBlockState(long x, long y, long z) {
        SubChunk subChunk = chunks[(int) (y >> 4)];
        if(subChunk == null) {
            return Blocks.AIR.getDefaultState();
        }
        return subChunk.getBlock((int) (x % 15), (int) (y % 15), (int) (z % 15));
    }

    @Override
    public void setBlockState(long x, long y, long z, IBlockState blockState) {

    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void forEach(Consumer<ISubChunk> consumer) {

    }

    @Override
    public boolean isGenerated() {
        return generated;
    }

    @Override
    public boolean isPopulated() {
        return populated;
    }

    @Override
    public boolean isStructured() {
        return structured;
    }

    public void setBlockState(long x, long y, long z, BlockState blockState) {
        SubChunk subChunk = chunks[(int) (y >> 4)];
        if(subChunk == null) {
            subChunk = chunks[(int) (y >> 4)] = new SubChunk(world,x * 16L,(y >> 4) * 16,z * 16L);
        }
        subChunk.setBlockState((int) (x % 15), (int) (y % 15), (int) (z % 15),blockState);
    }
}
