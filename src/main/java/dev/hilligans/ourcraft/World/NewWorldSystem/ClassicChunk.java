package dev.hilligans.ourcraft.World.NewWorldSystem;

import dev.hilligans.ourcraft.Block.BlockState.IBlockState;
import dev.hilligans.ourcraft.Block.Blocks;

import java.util.function.Consumer;

public class ClassicChunk implements IChunk {

    public int x;
    public int z;
    public int height;

    public ISubChunk[] chunks;
    public IWorld world;

    public boolean dirty;

    public boolean generated = false;
    public boolean populated = false;
    public boolean structured = false;

    public ClassicChunk(IWorld world, int height, int x, int z) {
        this.world = world;
        this.height = height;
        this.x = x;
        this.z = z;
        chunks = new ISubChunk[height];
    }

    @Override
    public int getWidth() {
        return 16;
    }

    @Override
    public int getHeight() {
        return height;
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
        if(y < 0) {
            return Blocks.AIR.getDefaultState1();
        }
        ISubChunk subChunk = chunks[(int) (y >> 4)];
        if(subChunk == null) {
            return Blocks.AIR.getDefaultState1();
        }
        return subChunk.getBlockState((int) (x % 15), (int) (y % 15), (int) (z % 15));
    }

    @Override
    public void setBlockState(long x, long y, long z, IBlockState blockState) {
        ISubChunk subChunk = chunks[(int) (y >> 4)];
        if(subChunk == null) {
            if(blockState == Blocks.AIR.getDefaultState1()) {
                return;
            }
            subChunk = chunks[(int) (y >> 4)] = new SimpleSubChunkImpl(16,16);
        }
        if(subChunk.setBlockState((int) (x % 15), (int)(y % 15), (int) (z % 15),blockState) != blockState) {
            dirty = true;
        }
    }

    @Override
    public void setChunkPosition(long x, long y, long z) {
        this.x = (int) x;
        this.z = (int) z;
    }

    @Override
    public IWorld getWorld() {
        return world;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void forEach(Consumer<ISubChunk> consumer) {
        for(ISubChunk subChunk : chunks) {
            consumer.accept(subChunk);
        }
    }

    @Override
    public void setDirty(boolean value) {
        dirty = value;
    }

    @Override
    public boolean isDirty() {
        return dirty;
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
}
