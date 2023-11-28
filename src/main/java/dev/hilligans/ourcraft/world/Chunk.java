package dev.hilligans.ourcraft.world;

import dev.hilligans.ourcraft.biome.Biome;
import dev.hilligans.ourcraft.block.blockstate.IBlockState;
import dev.hilligans.ourcraft.client.rendering.world.managers.VAOManager;
import dev.hilligans.ourcraft.data.other.blockstates.BlockState;
import dev.hilligans.ourcraft.block.Blocks;
import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.data.primitives.Tuple;
import dev.hilligans.ourcraft.entity.Entity;
import dev.hilligans.ourcraft.util.Settings;
import dev.hilligans.ourcraft.world.newworldsystem.IChunk;
import dev.hilligans.ourcraft.world.newworldsystem.ISubChunk;
import dev.hilligans.ourcraft.world.newworldsystem.IWorld;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2IntOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import static org.lwjgl.opengl.GL15.glGetBufferSubData;

public class Chunk implements IChunk {

    public ArrayList<SubChunk> chunks = new ArrayList<>();

    public World world;

    public Short2ObjectOpenHashMap<DataProvider> dataProviders = new Short2ObjectOpenHashMap<>();

    public Int2ObjectOpenHashMap<Entity> entities = new Int2ObjectOpenHashMap<>();
    //public Long2ObjectOpenHashMap<ArrayList<BlockPos>> blockTicks = new Long2ObjectOpenHashMap<>();
    public Short2IntOpenHashMap heightMap = new Short2IntOpenHashMap();

    public boolean needsSaving = false;


    public int x;
    public int z;

    public static int terrain = 64;

    public boolean populated = false;

    public void tick() {
        /*
        if(world.isServer()) {
            ArrayList<BlockPos> list = blockTicks.get(((ServerWorld) world).server.getTime());
            if(list != null) {
                blockTicks.remove(((ServerWorld) world).server.getTime());
                for(BlockPos pos : list) {
                    world.getBlockState(pos).getBlock().tickBlock(world,pos);
                }
            }
        }

         */
    }

   // public void scheduleTick(BlockPos pos, int time) {
   //     if(world.isServer()) {
   //         long futureTime = ((ServerWorld)world).server.getTime() + time;
   //         ArrayList<BlockPos> list = blockTicks.computeIfAbsent(futureTime, k -> new ArrayList<>());
   //         list.add(pos);
   //     }
   // }

    public void setWorld(World world) {
        this.world = world;
        for(SubChunk subChunk : chunks) {
            subChunk.world = world;
        }
    }

    public void destroyMap(int newId) {
        if(id != -1 && id != -2 && id != -3) {
            VAOManager.destroyBuffer(id);
        }
        this.id = newId;
    }

    public int getBlockHeight(int x, int z) {
        Biome biome = getBiome1(x, z);
        double val = world.simplexNoise.getHeight(x, z, biome.terrainHeights);
        return (int) (val + terrain);
    }

    public int interpolate(int height, int xHeight) {
        return Math.round(((float)height + xHeight) / 2);
    }

    public Biome getBiome1(int x, int z) {
        return world.biomeMap.getBiome(x,z);
    }

    @Override
    public int getWidth() {
        return 16;
    }

    @Override
    public int getHeight() {
        return 256;
    }

    @Override
    public long getX() {
        return x;
    }

    @Override
    public long getY() {
        return 0;
    }

    @Override
    public long getZ() {
        return z;
    }

    @Override
    public ISubChunk get(long blockX, long blockY, long blockZ) {
        return null;
    }

    @Override
    public IBlockState getBlockState1(long x, long y, long z) {
        return null;
    }

    @Override
    public void setBlockState(long x, long y, long z, IBlockState blockState) {

    }

    @Override
    public void setChunkPosition(long x, long y, long z) {

    }

    @Override
    public IWorld getWorld() {
        return null;
    }

    @Override
    public IChunk setWorld(IWorld world) {
        return this;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int getSubChunkCount() {
        return 0;
    }

    @Override
    public void forEach(Consumer<ISubChunk> consumer) {

    }

    @Override
    public void replace(UnaryOperator<ISubChunk> replacer) {

    }

    @Override
    public void setDirty(boolean value) {

    }

    @Override
    public boolean isDirty() {
        return false;
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

    public BlockState getBlockState(long x, long y, long z) {
        if(y >= Settings.chunkHeight * 16 || y < 0) {
            return Blocks.AIR.getDefaultState();
        }
        int pos = (int) (y >> 4);
        SubChunk subChunk = chunks.get(pos);
        return subChunk.getBlock((int) (x & 15), (int) (y & 15), (int) (z & 15));
    }

    public BlockState getBlockState(BlockPos blockPos) {
        return getBlockState(blockPos.x,blockPos.y,blockPos.z);
    }

    public DataProvider getDataProvider(BlockPos pos) {
        needsSaving = true;
        //int height = pos.y >> 4;
        //SubChunk subChunk = chunks.get(height);
        return dataProviders.get((short)(pos.x & 15 | (pos.y & 255) << 4 | (pos.z & 15) << 12));
        //return subChunk.getDataProvider(pos.x & 15, pos.y & 15, pos.z & 15);
    }

    public void setDataProvider(BlockPos pos, DataProvider dataProvider) {
        needsSaving = true;
        //int height = pos.y >> 4;
        //SubChunk subChunk = chunks.get(height);
        dataProviders.put((short)(pos.x & 15 | (pos.y & 255) << 4 | (pos.z & 15) << 12),dataProvider);
        //subChunk.setDataProvider(pos.x & 15, pos.y & 15, pos.z & 15, dataProvider);
    }

    public void setBlockState(long x, long y, long z, BlockState blockState) {
        needsSaving = true;
        if(y > Settings.chunkHeight * 16 || y < 0) {
           return;
        }

        if(populated) {
            short height = (short) (x << 4 | z);
            if (blockState.getBlock().blockProperties.airBlock) {
                if (heightMap.get(height) == y) {
                    for (int i = (int) y; i > 0; i--) {
                        if (!getBlockState(x, i, z).getBlock().blockProperties.airBlock) {
                            heightMap.put(height, i);
                        }
                    }
                }
            } else {
                if (heightMap.get(height) < y) {
                    heightMap.put(height, (int) y);
                }
            }
        }
        int pos = (int) (y >> 4);
        chunks.get(pos).setBlockState((int) x, (int) y, (int) z,blockState);
    }

    public BlockPos getHeight(int x, int z) {
        return new BlockPos(x,heightMap.get((short)((x << 4) | z)),z);
    }

    public int getHeightInt(int x, int z) {
        return heightMap.get((short)(((x & 0xF ) << 4) | (z  /*& 0xF */)));
    }

    public ArrayList<Tuple<BlockState,Integer>> getBlockChainedList() {
        BlockState currentState = null;
        ArrayList<Tuple<BlockState,Integer>> values = new ArrayList<>();
        int count = 0;
        for(int i = 0; i < 16 * 16 * Settings.chunkHeight * 16; i++) {
            int x = i & 15;
            int y = i >> 4 & 255;
            int z = i >> 12 & 15;
            BlockState newState = getBlockState(x, y, z);
            if (!newState.equals(currentState)) {
                if (currentState != null) {
                    values.add(new Tuple<>(currentState, count));
                    count = 0;
                }
                currentState = newState;
            }
            count++;
        }
        return values;
    }

    public int id = -1;

    @Override
    public String toString() {
        return "Chunk{" +
                "chunks=" + chunks +
                ", x=" + x +
                ", z=" + z +
                '}';
    }
}


