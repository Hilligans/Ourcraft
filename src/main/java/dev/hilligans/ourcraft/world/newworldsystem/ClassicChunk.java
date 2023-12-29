package dev.hilligans.ourcraft.world.newworldsystem;

import dev.hilligans.ourcraft.block.blockstate.IBlockState;
import dev.hilligans.ourcraft.block.Blocks;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

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
    public ISubChunk get(long blockX, long blockY, long blockZ) {
        return chunks[(int) (blockY >> 4)];
    }

    @Override
    public IBlockState getBlockState1(long x, long y, long z) {
        if(y < 0) {
            return Blocks.AIR.getDefaultState();
        }
        ISubChunk subChunk = chunks[(int) (y >> 4)];
        if(subChunk == null) {
            return Blocks.AIR.getDefaultState();
        }
        return subChunk.getBlockState(world, (int) (x % 15), (int) (y % 15), (int) (z % 15));
    }

    @Override
    public void setBlockState(long x, long y, long z, IBlockState blockState) {
        ISubChunk subChunk = chunks[(int) (y >> 4)];
        if(subChunk == null) {
            if(blockState == Blocks.AIR.getDefaultState()) {
                return;
            }
            subChunk = chunks[(int) (y >> 4)] = new SimpleSubChunkImpl(16,16);
        }
        if(subChunk.setBlockState(world, (int) (x % 15), (int)(y % 15), (int) (z % 15),blockState) != blockState) {
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
    public IChunk setWorld(IWorld world) {
        this.world = world;
        return this;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int getSubChunkCount() {
        return 0;
    }

    @Override
    public void forEach(Consumer<ISubChunk> consumer) {
        for(ISubChunk subChunk : chunks) {
            consumer.accept(subChunk);
        }
    }

    @Override
    public void replace(UnaryOperator<ISubChunk> replacer) {
        if(chunks != null) {
            for(int x = 0; x < chunks.length; x++) {
                chunks[x] = replacer.apply(chunks[x]);
            }
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
