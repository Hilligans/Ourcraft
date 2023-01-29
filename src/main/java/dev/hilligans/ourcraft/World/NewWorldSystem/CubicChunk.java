package dev.hilligans.ourcraft.World.NewWorldSystem;

import dev.hilligans.ourcraft.Block.BlockState.IBlockState;
import dev.hilligans.ourcraft.Block.Blocks;

import java.util.function.Consumer;

public class CubicChunk implements IChunk {

    public int size;
    public int x;
    public int y;
    public int z;

    public IWorld world;
    public ISubChunk[] subChunks;

    public short flags = 0;


    public CubicChunk(IWorld world, int size, int x, int y, int z) {
        this.world = world;
        this.size = size;
        this.x = x;
        this.y = y;
        this.z = z;
        int width = size >> 4;
        this.subChunks = new ISubChunk[width * width * width];
    }


    @Override
    public int getWidth() {
        return size;
    }

    @Override
    public int getHeight() {
        return size;
    }

    @Override
    public long getX() {
        return x;
    }

    @Override
    public long getY() {
        return y;
    }

    @Override
    public long getZ() {
        return z;
    }

    @Override
    public IBlockState getBlockState1(long x, long y, long z) {
        if(subChunks == null) {
            return Blocks.AIR.getDefaultState1();
        }
        //TODO fix
        int index = getIndex(x & 31, y & 31, z & 31);
        if(subChunks[index] == null) {
            return Blocks.AIR.getDefaultState1();
        }
        return subChunks[index].getBlockState(x & 15, y & 15, z & 15);
    }

    @Override
    public void setBlockState(long x, long y, long z, IBlockState blockState) {
        int width = size >> 4;
        if(subChunks == null && blockState != null) { //&& !blockState.getBlock().getIdentifierName().equals("ourcraft:air")) {
            subChunks = new ISubChunk[width * width * width];
        }
        //TODO fix
        int index = getIndex(x & 31, y & 31, z & 31);
        ISubChunk subChunk = subChunks[index];
        if(subChunk == null) {
            subChunk = new SimpleSubChunkImpl(16,16);
            subChunks[index] = subChunk;
        }
        subChunk.setBlockState(x & 15, y & 15, z & 15, blockState);
    }

    @Override
    public void setChunkPosition(long x, long y, long z) {
        this.x = (int) x;
        this.y = (int) y;
        this.z = (int) z;
    }

    @Override
    public IWorld getWorld() {
        return world;
    }

    @Override
    public boolean isEmpty() {
        return subChunks == null;
    }

    @Override
    public void forEach(Consumer<ISubChunk> consumer) {
        if(subChunks != null) {
            for(ISubChunk subChunk : subChunks) {
                consumer.accept(subChunk);
            }
        }
    }

    @Override
    public void setDirty(boolean value) {
        int D = 1;
        if(value) {
           // System.out.println(flags);
            flags |= D;
           // System.out.println(flags);
        } else {
            flags &=~D;
        }
    }

    @Override
    public boolean isDirty() {
        return (flags & 1) != 0;
    }

    @Override
    public boolean isGenerated() {
        return false;
    }

    @Override
    public boolean isPopulated() {
        return false;
    }

    @Override
    public boolean isStructured() {
        return false;
    }

    int getIndex(long x, long y, long z) {
        int width = size >> 4;
        return (int) (((x >> 4) * width + (y >> 4)) * width + (z >> 4));
    }
}
