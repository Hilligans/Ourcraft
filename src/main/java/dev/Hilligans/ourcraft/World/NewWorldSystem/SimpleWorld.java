package dev.Hilligans.ourcraft.World.NewWorldSystem;

import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Block.BlockState.IBlockState;
import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import dev.Hilligans.ourcraft.Data.Other.BlockStates.BlockState;
import dev.Hilligans.ourcraft.Data.Other.BoundingBox;

import java.util.Random;
import java.util.function.Consumer;

public class SimpleWorld implements IServerWorld, IMethodResult {

    public int worldID;
    public String worldName;
    public boolean randomTick = false;

    public float randomTickSpeed = 3f / (16*16*16);

    public IThreeDChunkContainer chunkContainer;

    public SimpleWorld(int id, String name) {
        this.worldID = id;
        this.worldName = name;
        chunkContainer = new NewChunkContainer();
    }


    @Override
    public String getName() {
        return worldName;
    }

    @Override
    public int getID() {
        return worldID;
    }

    @Override
    public void tick() {
        SimpleWorld self = this;
        if(randomTick) {
            int width = chunkContainer.getChunkWidth();
            int height = chunkContainer.getChunkHeight();
            int volume = width * width * height;
            int tickCount = Math.round(volume * randomTickSpeed);

            Random random = new Random();
            BlockPos pos = new BlockPos(0, 0, 0);
            //TODO implement using subchunks to avoid ticking random subchunks with air in them.
            chunkContainer.forEach(chunk -> {
                if (!chunk.isEmpty()) {
                    for (int i = 0; i < tickCount; i++) {
                        int val = random.nextInt(volume);
                        int x = val % width;
                        val /= width;
                        int z = val % width;
                        val /= width;
                        int y = val;
                        pos.set(x, y, z);
                        IBlockState blockState = chunk.getBlockState1(x, y, z);
                        blockState.getBlock().randomTick(self, blockState, self, pos, random);
                    }
                }
            });
        }

    }

    @Override
    public IChunk getChunk(long blockX, long blockY, long blockZ) {
        return chunkContainer.getChunk(blockX, blockY, blockZ);
    }

    @Override
    public IChunk getChunkNonNull(long blockX, long blockY, long blockZ) {
        return null;
    }

    @Override
    public void setChunk(long blockX, long blockY, long blockZ, IChunk chunk) {
        chunkContainer.setChunk(blockX,blockY,blockZ,chunk);
    }

    @Override
    public IThreeDChunkContainer getChunkContainer() {
        return chunkContainer;
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
