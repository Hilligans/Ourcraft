package dev.hilligans.ourcraft.world.newworldsystem;

import java.lang.foreign.MemorySegment;

public class MMapContainer {

    MemorySegment masterSegment;

    public IWorld world;

    public int x;
    public int y;
    public int z;

    public int width;
    public int height;

    public MMapContainer(IWorld world, int x, int y, int z, int width, int height) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
    }

    public CubicChunk createChunk(int chunkX, int chunkY, int chunkZ) {
        return null;
    }
}
