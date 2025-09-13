package dev.hilligans.ourcraft.world.newworldsystem;

import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.block.blockstate.IBlockState;
import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.engine.data.BoundingBox;
import dev.hilligans.ourcraft.server.IServer;
import dev.hilligans.ourcraft.server.concurrent.ChunkLocker;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class SimpleServerWorld extends SimpleWorld implements IServerWorldBase, IMethodResult, IFeaturePlacerHelper {

    public IServer server;
    public BlockPos featurePlacerPosition = new BlockPos(0, 0, 0);

    public ConcurrentLinkedQueue<Consumer<IServerWorld>> postTickFutures = new ConcurrentLinkedQueue<>();

    public SimpleServerWorld(int id, String name) {
        super(id, name);
    }

    public IChunk getGeneratedChunk(int xx, int zz) {
        Random random = new Random();
        IChunk chunk = new ClassicChunk(this, 16, xx, zz);
        for(int x = 0; x < 16; x++) {
            for(int y = 0; y < chunk.getHeight(); y++) {
                for(int z = 0; z < 16; z++) {
                    if(y < 60) {
                       // chunk.setBlockState(x, y, z, Blocks.STONE.getDefaultState());
                    } else {
                       // chunk.setBlockState(x, y, z, Blocks.AIR.getDefaultState());
                        if(y % 2 == 0) {
                         //   chunk.setBlockState(x, y, z, Blocks.RED.getDefaultState());
                        } else {
                          //  chunk.setBlockState(x, y, z, Blocks.AIR.getDefaultState());
                        }
                    }


                    //chunk.setBlockState(x, y, z, (x % 2 == 1 ^ z % 2 == 1 ^ y % 2 == 1) ? Blocks.STONE.getDefaultState1() : Blocks.AIR.getDefaultState1());
                }
            }
        }
        return chunk;
    }

    @Override
    public ConcurrentLinkedQueue<Consumer<IServerWorld>> getPostTickQueue() {
        return postTickFutures;
    }

    @Override
    public IChunk getChunkNonNull(long blockX, long blockY, long blockZ) {
        IChunk chunk = super.getChunk(blockX, blockY, blockZ);
        if(chunk == null) {
            chunk = getGeneratedChunk((int) (blockX >> 4), (int) (blockZ >> 4));
            setChunk(blockX, blockY, blockZ, chunk);
        }
        return chunk;
    }

    @Override
    public void setServer(IServer server) {
        this.server = server;
    }

    @Override
    public IServer getServer() {
        return server;
    }

    @Override
    public IWorldGenerator getWorldGenerator() {
        return null;
    }

    @Override
    public ChunkLocker getChunkLocker() {
        return null;
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

    @Override
    public IWorld getParentWorld() {
        return this;
    }

    @Override
    public BlockPos getBlockPos() {
        return featurePlacerPosition;
    }

    @Override
    public void setState(BlockPos pos, IBlockState blockState) {
        setBlockState(pos.x + featurePlacerPosition.y, pos.y + featurePlacerPosition.y, pos.z + featurePlacerPosition.z, blockState);
    }

    @Override
    public void setState(long x, long y, long z, IBlockState blockState) {
        setBlockState(x + featurePlacerPosition.x, y + featurePlacerPosition.y, z + featurePlacerPosition.z, blockState);
    }

    @Override
    public void setStateRaw(BlockPos pos, IBlockState blockState) {
        setBlockState(pos, blockState);
    }

    @Override
    public void setStateRaw(long x, long y, long z, IBlockState blockState) {
        setBlockState(x, y, z, blockState);
    }

    @Override
    public IBlockState getState(BlockPos blockPos) {
        return getBlockState(blockPos.x + featurePlacerPosition.x, blockPos.y + featurePlacerPosition.y, blockPos.z + featurePlacerPosition.z);
    }

    @Override
    public IBlockState getState(long x, long y, long z) {
        return getBlockState(x + featurePlacerPosition.x, y + featurePlacerPosition.y, z + featurePlacerPosition.z);
    }

    @Override
    public IBlockState getStateRaw(BlockPos blockPos) {
        return getBlockState(blockPos);
    }

    @Override
    public IBlockState getStateRaw(long x, long y, long z) {
        return getBlockState(x, y, z);
    }

    @Override
    public void setPlacementPosition(BlockPos pos) {
        this.featurePlacerPosition.set(pos);
    }
}
