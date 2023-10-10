package dev.hilligans.ourcraft.Data.Other;

public class ChunkPos {

    public long chunkX;
    public long chunkY;
    public long chunkZ;

    public ChunkPos(long chunkX, long chunkY, long chunkZ) {
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.chunkZ = chunkZ;
    }

    public ChunkPos(BlockPos pos) {

    }

    public ChunkPos(int x, int y, int z) {

    }

    public long toLong() {
        return 0;
    }

    public static long fromBlockPos(BlockPos pos) {
        return (long)(pos.x) >> 4 & 4294967295L | ((long)(pos.z) >> 4 & 4294967295L) << 32;
    }

    public static long fromPos(int x, int z) {
        return (long)(x) >> 4 & 4294967295L | ((long)(z) >> 4 & 4294967295L) << 32;
    }

}
