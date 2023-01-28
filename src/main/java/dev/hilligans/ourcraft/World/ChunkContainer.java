package dev.hilligans.ourcraft.World;

import dev.hilligans.ourcraft.World.NewWorldSystem.IChunkContainer;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.function.Consumer;

public class ChunkContainer implements IChunkContainer {

    public Int2ObjectOpenHashMap<Int2ObjectOpenHashMap<Chunk>> chunks = new Int2ObjectOpenHashMap<>();

    public int cacheSize;
    public Chunk[] chunkCache;


    public ChunkContainer() {
        this(4);
    }

    public ChunkContainer(int cacheSize) {
        this.cacheSize = cacheSize;
        chunkCache = new Chunk[cacheSize];
    }

    @Override
    public Chunk getChunk(int x, int z) {
        Chunk chunk = getCache(x, z);
        if (chunk != null) {
            return chunk;
        }
        try {
            Int2ObjectOpenHashMap<Chunk> zMap = chunks.getOrDefault(x, null);
            if (zMap != null) {
                chunk = zMap.getOrDefault(z, null);
            }
        } catch(Exception e){}
        return chunk;
    }

    @Override
    public Chunk setChunk(int x, int z, Chunk chunk) {
        cache(chunk);
        Int2ObjectOpenHashMap<Chunk> zMap = chunks.computeIfAbsent(x,a -> new Int2ObjectOpenHashMap<>());
        try {
            return zMap.put(z,chunk);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Chunk removeChunk(int x, int z) {
        removeFromCache(x,z);
        Int2ObjectOpenHashMap<Chunk> zMap = chunks.getOrDefault(x,null);
        if(zMap != null) {
            return zMap.remove(z);
        }
        return null;
    }

    public Chunk removeChunk(long pos) {
        return removeChunk((int)pos,(int)(pos >> 32));
    }

    @Override
    public int getSize() {
        int size = 0;
        for(int val : chunks.keySet()) {
            Int2ObjectOpenHashMap<Chunk> zMap = chunks.getOrDefault(val,null);
            size += zMap.size();
        }
        return size;
    }

    @Override
    public void forEach(Consumer<Chunk> consumer) {
        for(int val : chunks.keySet()) {
            Int2ObjectOpenHashMap<Chunk> zMap = chunks.getOrDefault(val,null);
            zMap.forEach((integer, chunk) -> consumer.accept(chunk));
        }
    }

    public void cache(Chunk chunk) {
        for(int x = cacheSize - 1; x > 0; x--) {
            chunkCache[x] = chunkCache[x - 1];
        }
        chunkCache[0] = chunk;
    }

    public Chunk getCache(int x, int z) {
        for(int i = 0; i < cacheSize; i++) {
            if(chunkCache[i] != null) {
                if (chunkCache[i].x == x && chunkCache[i].z == z) {
                    return chunkCache[i];
                }
            }
        }
        return null;
    }

    public void removeFromCache(int x, int z) {
        for(int i = 0; i < cacheSize; i++) {
            if(chunkCache[i] != null) {
                if (chunkCache[i].x == x && chunkCache[i].z == z) {
                    chunkCache[i] = null;
                }
            }
        }
    }
}
