package dev.hilligans.ourcraft.World.NewWorldSystem;

import dev.hilligans.ourcraft.Block.BlockState.IBlockState;
import dev.hilligans.ourcraft.Block.Blocks;
import dev.hilligans.ourcraft.Data.Other.ChunkPos;
import dev.hilligans.ourcraft.Server.Concurrent.Lock;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class CubicChunk implements IAtomicChunk {

    static final VarHandle ARRAY_HANDLE = MethodHandles.arrayElementVarHandle(ISubChunk[].class);

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
        return subChunks[index].getBlockState((int) (x & 15), (int) (y & 15), (int) (z & 15));
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
            subChunk = new GlobalPaletteAtomicSubChunk();
            subChunks[index] = subChunk;
        }
        ISubChunk repl = subChunk.canInsertOrGetNext(blockState);
        if(repl != null) {
            repl.setBlockState((int) (x & 15), (int) (y & 15), (int) (z & 15), blockState);
            subChunks[index] = repl;
            return;
        }
        subChunk.setBlockState((int) (x & 15), (int) (y & 15), (int) (z & 15), blockState);
    }

    @Override
    public void setBlockStateAtomic(Lock lock, long x, long y, long z, IBlockState blockState) {
        if(!lock.hasLock(new ChunkPos(this.x, this.y, this.z))) {
            int index = getIndex(x & 31, y & 31, z & 31);
            ISubChunk subChunk;
            do {
                subChunk = (ISubChunk) ARRAY_HANDLE.getVolatile(subChunks, index);
                if(subChunk instanceof IAtomicSubChunk atomicSubChunk) {
                    ISubChunk newChunk = subChunk.canInsertOrGetNext(blockState);
                    if(newChunk != null) {
                        newChunk.setBlockState((int) (x & 15), (int) (y & 15), (int) (z & 15), blockState);
                        if(ARRAY_HANDLE.weakCompareAndSet(subChunks, index, subChunk, newChunk)) {
                            return;
                        }
                    } else {
                        atomicSubChunk.setBlockStateAtomic((int) (x & 15), (int) (y & 15), (int) (z & 15), blockState);
                    }
                } else {
                    lock.acquire(new ChunkPos(this.x, this.y, this.z));
                    setBlockState(x, y, z, blockState);
                    return;
                }
            } while(ARRAY_HANDLE.getVolatile(subChunks, index) != subChunk);
        } else {
            setBlockState(x, y, z, blockState);
        }
    }

    @Override
    public boolean swapBlockStateAtomic(Lock lock, long x, long y, long z, IBlockState expected, IBlockState to) {
        if(!lock.hasLock(new ChunkPos(this.x, this.y, this.z))) {
            int index = getIndex(x & 31, y & 31, z & 31);
            synchronized (this) {
                ISubChunk subChunk = subChunks[index];
                if(subChunk instanceof IAtomicSubChunk atomicSubChunk) {
                    return atomicSubChunk.swapBlockStateAtomic((int) (x & 15), (int) (y & 15), (int) (z & 15), expected, to);
                }
            }
            lock.acquire(new ChunkPos(this.x, this.y, this.z));
        }
        if(getBlockState1(x, y, z) == expected) {
            setBlockState(x, y, z, to);
            return true;
        }
        return false;
    }

    @Override
    public void replaceAtomic(UnaryOperator<ISubChunk> replacer) {
        if(subChunks != null) {
            for(int x = 0; x < subChunks.length; x++) {
                ISubChunk subChunk;
                ISubChunk newSubChunk;
                do {
                    subChunk = (ISubChunk) ARRAY_HANDLE.getVolatile(subChunks, x);
                    newSubChunk = replacer.apply(subChunk);
                } while (ARRAY_HANDLE.weakCompareAndSet(subChunks, x, subChunk, newSubChunk));
            }
        }
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
    public int getSubChunkCount() {
        return subChunks.length;
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
    public void replace(UnaryOperator<ISubChunk> replacer) {
        if(subChunks != null) {
            for(int x = 0; x < subChunks.length; x++) {
                subChunks[x] = replacer.apply(subChunks[x]);
            }
        }
    }

    @Override
    public void setDirty(boolean value) {
        short D = 1;
        if(value) {
            flags |= D;
        } else {
            flags &= (short) ~D;
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
