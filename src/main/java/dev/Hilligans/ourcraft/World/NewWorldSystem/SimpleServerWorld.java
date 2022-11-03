package dev.Hilligans.ourcraft.World.NewWorldSystem;

import dev.Hilligans.ourcraft.Biome.Biome;
import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Block.Blocks;
import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import dev.Hilligans.ourcraft.Data.Other.BoundingBox;
import dev.Hilligans.ourcraft.Server.MultiPlayerServer;
import dev.Hilligans.ourcraft.Util.Settings;
import dev.Hilligans.ourcraft.World.Chunk;

public class SimpleServerWorld extends SimpleWorld implements IServerWorld, IMethodResult {

    public MultiPlayerServer server;

    public SimpleServerWorld(int id, String name) {
        super(id, name);
    }

    public IChunk getGeneratedChunk(int xx, int zz) {
        IChunk chunk = new ClassicChunk(this, 256, xx, zz);
        for(int x = 0; x < 16; x++) {
            for(int y = 0; y < 64; y++) {
                for(int z = 0; z < 16; z++) {
                    chunk.setBlockState(x,y,z,Blocks.STONE.getDefaultState1());
                }
            }
        }
        return chunk;
    }

    @Override
    public IChunk getChunk(long blockX, long blockY, long blockZ) {
        IChunk chunk = super.getChunk(blockX, blockY, blockZ);
        if(chunk == null) {
            chunk = getGeneratedChunk((int) (blockX >> 4), (int) (blockZ >> 4));
            setChunk(blockX, blockY, blockZ, chunk);
        }
        return chunk;
    }

    @Override
    public IChunk getChunkNonNull(long blockX, long blockY, long blockZ) {
        return super.getChunkNonNull(blockX, blockY, blockZ);
    }

    @Override
    public void setServer(MultiPlayerServer server) {
        this.server = server;
    }

    @Override
    public void queueUpdate(int x, int y, int z) {

    }

    @Override
    public void queueUpdate(BlockPos pos) {

    }

    @Override
    public void queueSixUpdates(int x, int y, int z) {

    }

    @Override
    public void queueSixUpdates(BlockPos pos) {

    }

    @Override
    public void scheduleTick(int x, int y, int z, Block block, int delay) {

    }

    @Override
    public void scheduleTick(BlockPos pos, Block block, int delay) {

    }

    @Override
    public BlockPos getWorldSpawn(BoundingBox boundingBox) {
        return null;
    }
}
