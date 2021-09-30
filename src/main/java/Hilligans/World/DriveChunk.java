package Hilligans.World;

import Hilligans.Biome.Biome;
import Hilligans.Client.MatrixStack;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Data.Other.BlockStates.BlockState;
import Hilligans.Data.Primitives.DoubleTypeWrapper;
import Hilligans.WorldSave.ChunkLoader;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class DriveChunk extends Chunk {

    public AtomicBoolean loaded = new AtomicBoolean(false);
    public Consumer<DriveChunk> processor;

    public DriveChunk(int x, int z, World world, Consumer<DriveChunk> chunkProcessor) {
        super(x, z, world);
    }

    public void load() {
        loaded.set(true);
        Chunk chunk = ChunkLoader.readChunk(x,z);
        if(chunk != null) {
            this.chunks = chunk.chunks;
            this.entities = chunk.entities;
            this.blockTicks = chunk.blockTicks;
            this.heightMap = chunk.heightMap;
        }
        processor.accept(this);
    }

    public void tick() {
        if(!loaded.get()) {
            load();
        }
        super.tick();
    }

    public int getTotalVertices() {
        if(!loaded.get()) {
            load();
        }
        return super.getTotalVertices();
    }

    public void scheduleTick(BlockPos pos, int time) {
        if(!loaded.get()) {
            load();
        }
        super.scheduleTick(pos,time);
    }

    public void setWorld(World world) {
        if(!loaded.get()) {
            load();
        }
        super.setWorld(world);
    }

    public void render(MatrixStack matrixStack) {
        if(!loaded.get()) {
            load();
        }
        super.render(matrixStack);
    }

    public void destroy() {
        if(!loaded.get()) {
            load();
        }
        super.destroy();
    }

    public void generate() {
        if(!loaded.get()) {
            load();
        }
        super.generate();
    }

    public void populate() {
        if(!loaded.get()) {
            load();
        }
        super.populate();
    }

    public int getBlockHeight(int x, int z) {
        if(!loaded.get()) {
            load();
        }
        return super.getBlockHeight(x,z);
    }

    public Biome getBiome1(int x, int z) {
        if(!loaded.get()) {
            load();
        }
        return super.getBiome1(x,z);
    }

    public BlockState getBlockState(int x, int y, int z) {
        if(!loaded.get()) {
            load();
        }
        return super.getBlockState(x,y,z);
    }

    public BlockState getBlockState(BlockPos blockPos) {
        if(!loaded.get()) {
            load();
        }
        return super.getBlockState(blockPos);
    }

    public DataProvider getDataProvider(BlockPos pos) {
        if(!loaded.get()) {
            load();
        }
        return super.getDataProvider(pos);
    }

    public void setDataProvider(BlockPos pos, DataProvider dataProvider) {
        if(!loaded.get()) {
            load();
        }
        super.setDataProvider(pos,dataProvider);
    }

    public void setBlockState(int x, int y, int z, BlockState blockState) {
        if(!loaded.get()) {
            load();
        }
        super.setBlockState(x,y,z,blockState);
    }

    public BlockPos getHeight(int x, int z) {
        if(!loaded.get()) {
            load();
        }
        return super.getHeight(x,z);
    }

    public int getHeightInt(int x, int z) {
        if(!loaded.get()) {
            load();
        }
        return super.getHeightInt(x,z);
    }

    public void updateBlock(BlockPos pos) {
        if(!loaded.get()) {
            load();
        }
        super.updateBlock(pos);
    }

    public ArrayList<DoubleTypeWrapper<BlockState,Integer>> getBlockChainedList() {
        if(!loaded.get()) {
            load();
        }
        return super.getBlockChainedList();
    }

    public void setFromChainedList(ArrayList<DoubleTypeWrapper<BlockState,Integer>> values) {
        if(!loaded.get()) {
            load();
        }
        super.setFromChainedList(values);
    }

    public void buildMesh1() {
        if(!loaded.get()) {
            load();
        }
        super.buildMesh1();
    }
}
