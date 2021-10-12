package dev.Hilligans.Ourcraft.World;

import dev.Hilligans.Ourcraft.Biome.Biome;
import dev.Hilligans.Ourcraft.Client.MatrixStack;
import dev.Hilligans.Ourcraft.ClientMain;
import dev.Hilligans.Ourcraft.Data.Other.BlockPos;
import dev.Hilligans.Ourcraft.Data.Other.BlockStates.BlockState;
import dev.Hilligans.Ourcraft.Data.Primitives.Tuplet;
import dev.Hilligans.Ourcraft.WorldSave.ChunkLoader;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class DriveChunk extends Chunk {

    public AtomicBoolean loaded = new AtomicBoolean(false);
    public Consumer<DriveChunk> processor;
    public long time = 0;

    public DriveChunk(int x, int z, World world, Consumer<DriveChunk> chunkProcessor) {
        super(x, z, world);
        this.processor = chunkProcessor;
        if(world == null) {
            new Throwable().printStackTrace();
        }
    }

    public synchronized void load() {
        loaded.set(true);
        Chunk chunk = ChunkLoader.readChunk(x,z);
        if(chunk != null) {
            this.chunks = chunk.chunks;
            this.entities = chunk.entities;
            this.blockTicks = chunk.blockTicks;
            this.heightMap = chunk.heightMap;
        }
        setWorld(world);
        time = System.currentTimeMillis();
        processor.accept(this);
    }

    public synchronized void unload() {
        loaded.set(false);
        ClientMain.getClient().unloadQueue.add(id);
        id = -1;
        this.chunks.clear();
        this.entities.clear();
        this.blockTicks.clear();
        this.heightMap.clear();
    }

    public synchronized void tick() {
        if(!loaded.get()) {
            load();
        }
        time = System.currentTimeMillis();
        super.tick();
    }

    public synchronized int getTotalVertices() {
        if(!loaded.get()) {
            load();
        }
        time = System.currentTimeMillis();
        return super.getTotalVertices();
    }

    public synchronized void scheduleTick(BlockPos pos, int time) {
        if(!loaded.get()) {
            load();
        }
        this.time = System.currentTimeMillis();
        super.scheduleTick(pos,time);
    }

    public synchronized void setWorld(World world) {
        if(!loaded.get()) {
            load();
        }
        time = System.currentTimeMillis();
        super.setWorld(world);
    }

    public synchronized void render(MatrixStack matrixStack) {
        if(!loaded.get()) {
            load();
        }
        time = System.currentTimeMillis();
        super.render(matrixStack);
    }

    public synchronized void destroy() {
        if(!loaded.get()) {
            load();
        }
        time = System.currentTimeMillis();
        super.destroy();
    }

    public synchronized void generate() {
        if(!loaded.get()) {
            load();
        }
        time = System.currentTimeMillis();
        super.generate();
    }

    public synchronized void populate() {
        if(!loaded.get()) {
            load();
        }
        time = System.currentTimeMillis();
        super.populate();
    }

    public synchronized int getBlockHeight(int x, int z) {
        if(!loaded.get()) {
            load();
        }
        time = System.currentTimeMillis();
        return super.getBlockHeight(x,z);
    }

    public synchronized Biome getBiome1(int x, int z) {
        if(!loaded.get()) {
            load();
        }
        time = System.currentTimeMillis();
        return super.getBiome1(x,z);
    }

    public synchronized BlockState getBlockState(int x, int y, int z) {
        if(!loaded.get()) {
            load();
        }
        time = System.currentTimeMillis();
        return super.getBlockState(x,y,z);
    }

    public synchronized BlockState getBlockState(BlockPos blockPos) {
        if(!loaded.get()) {
            load();
        }
        time = System.currentTimeMillis();
        return super.getBlockState(blockPos);
    }

    public synchronized DataProvider getDataProvider(BlockPos pos) {
        if(!loaded.get()) {
            load();
        }
        time = System.currentTimeMillis();
        return super.getDataProvider(pos);
    }

    public synchronized void setDataProvider(BlockPos pos, DataProvider dataProvider) {
        if(!loaded.get()) {
            load();
        }
        time = System.currentTimeMillis();
        super.setDataProvider(pos,dataProvider);
    }

    public synchronized void setBlockState(int x, int y, int z, BlockState blockState) {
        if(!loaded.get()) {
            load();
        }
        time = System.currentTimeMillis();
        super.setBlockState(x,y,z,blockState);
    }

    public synchronized BlockPos getHeight(int x, int z) {
        if(!loaded.get()) {
            load();
        }
        time = System.currentTimeMillis();
        return super.getHeight(x,z);
    }

    public synchronized int getHeightInt(int x, int z) {
        if(!loaded.get()) {
            load();
        }
        time = System.currentTimeMillis();
        return super.getHeightInt(x,z);
    }

    public synchronized void updateBlock(BlockPos pos) {
        if(!loaded.get()) {
            load();
        }
        time = System.currentTimeMillis();
        super.updateBlock(pos);
    }

    public synchronized ArrayList<Tuplet<BlockState,Integer>> getBlockChainedList() {
        if(!loaded.get()) {
            load();
        }
        time = System.currentTimeMillis();
        return super.getBlockChainedList();
    }

    public synchronized void setFromChainedList(ArrayList<Tuplet<BlockState,Integer>> values) {
        if(!loaded.get()) {
            load();
        }
        time = System.currentTimeMillis();
        super.setFromChainedList(values);
    }

    public synchronized void buildMesh1() {
        if(!loaded.get()) {
            load();
        }
        time = System.currentTimeMillis();
        super.buildMesh1();
    }
}
