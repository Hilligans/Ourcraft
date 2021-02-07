package Hilligans.World;

import Hilligans.Block.Blocks;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Entity.Entity;
import Hilligans.Network.ClientNetworkHandler;
import Hilligans.Network.Packet.Client.CRequestChunkPacket;
import Hilligans.Util.*;
import Hilligans.Util.Noises.*;
import Hilligans.World.Builders.WorldBuilder;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class World {

    public Int2ObjectOpenHashMap<Entity> entities = new Int2ObjectOpenHashMap<>();
    public ConcurrentLinkedQueue<BlockChange> blockChanges = new ConcurrentLinkedQueue<>();
    Long2ObjectOpenHashMap<Chunk> chunks = new Long2ObjectOpenHashMap<>();

    Noise noise = new Noise(131);
    Noise biomes = new Noise(new Random(131).nextInt());

    PerlinNoise heightMap;
    //PerlinNoise biomeMap;
    public BiomeNoise biomeMap;


    KenPerlinNoise height;

    SimplexNoise simplexNoise;

    public Random random;

   // public int simplexNoise;

    public ArrayList<WorldBuilder> worldBuilders = new ArrayList<>();

    public World() {
        random = new Random(131);
        biomeMap = new BiomeNoise(random);
        heightMap = new PerlinNoise(random.nextInt(),0.17,10,1,10);
        height = new KenPerlinNoise(random.nextInt());
        simplexNoise = new SimplexNoise(random);
        //simplexNoise = random.nextInt();
        //biomeMap = new PerlinNoise(random.nextInt(),20,30,1,100);
    }

    public Chunk getChunk(long chunkPos) {
        try {
            return chunks.get(chunkPos);
        } catch (ArrayIndexOutOfBoundsException ignored) {
            return null;
        }
    }

    public Chunk getChunk(int x, int z) {
        return getChunk((long)x & 4294967295L | ((long)z & 4294967295L) << 32);
    }

    public Chunk getOrGenerateChunk(int x, int z) {
        Chunk chunk = getChunk(x,z);
        if(chunk == null) {
            generateChunk(x,z);
        }
        chunk = getChunk(x,z);
        if(chunk == null) {
            System.out.println("CHUNK IS NULL");
        }
        return chunk;
    }

    public void generateChunk(int x, int z) {
        if(getChunk(x,z) == null) {
            Chunk chunk = new Chunk(x,z,this);
            chunks.put(x & 4294967295L | ((long)z & 4294967295L) << 32,chunk);
            chunk.generate();
        }
    }

    public BlockState getBlockState(int x, int y, int z) {
        Chunk chunk = getChunk(x >> 4,z >> 4);
        if(chunk == null) {
            return new BlockState(Blocks.AIR);
        }
        return chunk.getBlockState(x,y,z);

    }

    public BlockState getBlockState(BlockPos pos) {
        return getBlockState(pos.x,pos.y,pos.z);
    }

    public void setBlockState(int x, int y, int z, BlockState blockState) {
        if(y >= Settings.minHeight  && y < Settings.maxHeight) {
            Chunk chunk = getChunk(x >> 4, z >> 4);
            if (chunk == null) {
                return;
            }
            chunk.setBlockState(x, y, z, blockState);
        }
    }

    public void setBlockState(BlockPos pos, BlockState blockState) {
        setBlockState(pos.x,pos.y,pos.z,blockState);
    }

    public void tick() {
    }

    public void requestChunk(int x, int z) {
        for (ClientWorld.XZHolder requestedChunk : requestedChunks) {
            if (requestedChunk.x == x && requestedChunk.z == z) {
                return;
            }
        }
        requestedChunks.add(new ClientWorld.XZHolder(x,z));
        ClientNetworkHandler.sendPacket(new CRequestChunkPacket(x, z));
    }

    ConcurrentLinkedQueue<ClientWorld.XZHolder> requestedChunks = new ConcurrentLinkedQueue<>();

    public void setChunk(Chunk chunk) {
        for(ClientWorld.XZHolder xzHolder : requestedChunks) {
            if(xzHolder.x == chunk.x && xzHolder.z == chunk.z) {
                requestedChunks.remove(xzHolder);
                chunks.put(chunk.x & 4294967295L | ((long)chunk.z & 4294967295L) << 32,chunk);
                return;
            }
        }

        /*for(int x = 0; x < requestedChunks.size(); x++) {
            if(requestedChunks.get(x).x == chunk.x && requestedChunks.get(x).z == chunk.z) {
                requestedChunks.remove(x);
                chunks.put(chunk.x & 4294967295L | ((long)chunk.z & 4294967295L) << 32,chunk);
                return;
            }
        }

         */
    }

    public static final float stepCount = 0.05f;
    public static final int distance = 5;

    static final float offSet = -0.5f;

    public BlockPos traceBlock(float x, float y, float z, double pitch, double yaw) {
        Vector3d vector3d = new Vector3d();
        boolean placed = false;

        for(int a = 0; a < distance / stepCount; a++) {

            final double Z = z - Math.sin(yaw) * Math.cos(pitch) * a * 0.1 + offSet;
            final double Y = y - Math.sin(pitch) * 0.1 * a + offSet;
            final double X = (x - Math.cos(yaw) * Math.cos(pitch) * a * 0.1) + offSet;
            BlockState blockState = getBlockState((int) Math.round(X), (int) Math.round(Y), (int) Math.round(Z));
            if(blockState.block != Blocks.AIR) {
                placed = true;
                break;
            }
            vector3d.x = X;
            vector3d.y = Y;
            vector3d.z = Z;
        }

        if(placed) {
            return new BlockPos((int) Math.round(vector3d.x), (int) Math.round(vector3d.y), (int) Math.round(vector3d.z));
        } else {
            return null;
        }
    }

    public BlockPos traceBlockToBreak(float x, float y, float z, double pitch, double yaw) {

        for(int a = 0; a < distance / stepCount; a++) {

            final double Z = z - Math.sin(yaw) * Math.cos(pitch) * a * 0.1 + offSet;
            final double Y = y - Math.sin(pitch) * 0.1 * a + offSet;
            final double X = (x - Math.cos(yaw) * Math.cos(pitch) * a * 0.1) + offSet;
            BlockState blockState = getBlockState((int) Math.round(X), (int) Math.round(Y), (int) Math.round(Z));
            if(blockState.block != Blocks.AIR) {
                return new BlockPos((int) Math.round(X),(int) Math.round(Y),(int) Math.round(Z));
            }
        }
        return null;
    }

    public Chunk[] getChunksAround(int x, int z, int radius) {
        if(radius == 0) {
            return new Chunk[] {getChunk(x + 1,z),getChunk(x - 1,z),getChunk(x,z + 1),getChunk(x,z - 1)};
        }
        return null;
    }

    public abstract void addEntity(Entity entity);

    public abstract void removeEntity(int id);

    public static class BlockChange {
        public int x;
        public int z;
        public int y;
        public int id;

        public BlockChange(int x, int y, int z, int id) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.id = id;
        }

    }




}
