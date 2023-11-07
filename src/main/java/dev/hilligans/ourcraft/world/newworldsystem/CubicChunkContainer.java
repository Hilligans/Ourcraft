package dev.hilligans.ourcraft.world.newworldsystem;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class CubicChunkContainer extends EmptyContainer<IChunk> implements IThreeDChunkContainer {

    public int chunkWidth;
    public int chunkHeight;

    public IChunk cachedChunk;

    public CubicChunkContainer(int chunkWidth, int chunkHeight) {
        this.chunkWidth = chunkWidth;
        this.chunkHeight = chunkHeight;
    }

    @Override
    public synchronized IChunk getChunk(long x, long y, long z) {
        //concurrency reasons
        IChunk chunk = cachedChunk;
        if(chunk != null && chunk.getX() == x && chunk.getY() == y && chunk.getZ() == z) {
            return chunk;
        }
        Int2ObjectOpenHashMap<IChunk> c = container.get(to(x,y));
        if(c == null) {
            return null;
        }
        chunk = c.getOrDefault((int)z, null);
        cachedChunk = chunk;
        return chunk;
    }

    @Override
    public int getChunkWidth() {
        return chunkWidth;
    }

    @Override
    public int getChunkHeight() {
        return chunkHeight;
    }
}
