package dev.hilligans.ourcraft.world;

import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.data.other.blockstates.BlockState;
import dev.hilligans.ourcraft.block.Blocks;
import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.data.other.BoundingBox;
import dev.hilligans.ourcraft.data.other.RayResult;
import dev.hilligans.ourcraft.entity.entities.ItemEntity;
import dev.hilligans.ourcraft.entity.Entity;
import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.item.ItemStack;
import dev.hilligans.ourcraft.network.packet.server.SSendBlockChanges;
import dev.hilligans.ourcraft.ServerMain;
import dev.hilligans.ourcraft.world.builders.WorldBuilder;
import dev.hilligans.ourcraft.util.noises.BiomeNoise;
import dev.hilligans.ourcraft.util.noises.SimplexNoise;
import dev.hilligans.ourcraft.util.Settings;
import dev.hilligans.ourcraft.world.newworldsystem.IChunkContainer;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class World {

    public Int2ObjectOpenHashMap<Entity> entities = new Int2ObjectOpenHashMap<>();
    public ConcurrentLinkedQueue<BlockChange> blockChanges = new ConcurrentLinkedQueue<>();

    public GameInstance gameInstance;

    long seed = 1342;

    public int chunkCount = 0;

    public BiomeNoise biomeMap;


    SimplexNoise simplexNoise;

    public Random random;
    public IChunkContainer chunkContainer = new ChunkContainer();


    public ArrayList<WorldBuilder> worldBuilders = new ArrayList<>();

    public World(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        random = new Random(seed);
        biomeMap = new BiomeNoise(gameInstance,random);
        simplexNoise = new SimplexNoise(random);

    }

    public abstract boolean isServer();


    public Chunk getChunk(int x, int z) {
        return chunkContainer.getChunk(x,z);
    }

    public void putChunk(int x, int z, Chunk chunk) {
        chunkCount += chunkContainer.setChunk(x,z,chunk) == null ? 1 : 0;
    }
    public BlockState getBlockState(int x, int y, int z) {
        Chunk chunk = getChunk(x >> 4,z >> 4);
        if(chunk == null) {
            return Blocks.AIR.getDefaultState();
        }
        return chunk.getBlockState(x,y,z);
    }

    public void setBlockMatches(BlockPos pos, BlockState state, Block block) {
        if(getBlockState(pos).getBlock() == block) {
            setBlockState(pos,state);
        }
    }

    public DataProvider getDataProvider(BlockPos pos) {
        Chunk chunk = getChunk(pos.x >> 4,pos.z >> 4);
        if(chunk == null) {
            return null;
        }
        return chunk.getDataProvider(pos);
    }

    public void setDataProvider(BlockPos pos, DataProvider dataProvider) {
        Chunk chunk = getChunk(pos.x >> 4,pos.z >> 4);
        if(chunk != null) {
            chunk.setDataProvider(pos,dataProvider);
        }
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
        if (isServer()) {
            ServerMain.getServer().sendPacket(new SSendBlockChanges(x,y,z,blockState));
        }
        updateBlock(new BlockPos(x,y,z));
    }

    private void updateBlock(BlockPos pos) {
        for(int x = 0; x < 6; x++) {
            BlockPos newPos = pos.add(Block.getBlockPos(x));
            if(newPos.y >= Settings.minHeight  && newPos.y < Settings.maxHeight) {
                Chunk chunk = getChunk(newPos.x >> 4, newPos.z >> 4);
                if (chunk == null) {
                    return;
                }
                chunk.updateBlock(newPos);
            }
        }
    }

    public void spawnItemEntity(float x, float y, float z, ItemStack itemStack) {
        if(!itemStack.isEmpty()) {
            ItemEntity itemEntity = new ItemEntity(x, y, z, Entity.getNewId(), itemStack);
            itemEntity.velY = 0.30f;
            itemEntity.velX = (float) (Math.random() * 0.4 - 0.2f);
            itemEntity.velZ = (float) (Math.random() * 0.4 - 0.2f);
            itemEntity.pickupDelay = 10;
            addEntity(itemEntity);
        }
    }

    public void setBlockState(BlockPos pos, BlockState blockState) {
        setBlockState(pos.x,pos.y,pos.z,blockState);
    }

    public void tick() {
    }


    public HashSet<Long> set = new HashSet<>();

    public void setChunk(Chunk chunk, int x, int z) {
        chunk.setWorld(this);
        chunk.x = x;
        chunk.z = z;
        putChunk(x,z,chunk);
    }

    public static final double stepCount = 0.01;
    public static final int distance = 5;

    static final float offSet = -0.5f;

    public RayResult traceBlock(double x, double y, double z, double pitch, double yaw) {
        Vector3d vector3d = new Vector3d();
        boolean placed = false;
        boolean isAir = true;
        int side = -1;

        double addX = (Math.cos(yaw) * Math.cos(pitch)) * stepCount;
        double addY = Math.sin(pitch) * stepCount;
        double addZ = Math.sin(yaw) * Math.cos(pitch) * stepCount;

        double Z = z + offSet;
        double Y = y + offSet;
        double X = x + offSet;


        for(int a = 0; a < distance / stepCount; a++) {
            Z -= addZ;
            Y -= addY;
            X -= addX;
            BlockPos pos = new BlockPos((int) Math.round(X), (int) Math.round(Y), (int) Math.round(Z));
            BlockState blockState = getBlockState(pos);
            if(blockState.getBlock() != Blocks.AIR) {
                Vector3f vector3f = new Vector3f((float)X - offSet,(float)Y - offSet,(float)Z - offSet);
                if(blockState.getBlock().getBoundingBox(this,pos).intersectVector(vector3f, pos)) {
                    placed = true;
                    side = blockState.getBlock().getBoundingBox(this,pos).getHitSide(new Vector3f((float)vector3d.x - offSet,(float)vector3d.y - offSet,(float)vector3d.z - offSet), pos);
                    break;
                } else {
                    isAir = false;
                }
            } else {
                isAir = true;
            }
            vector3d.x = X;
            vector3d.y = Y;
            vector3d.z = Z;
        }

        if(placed) {
            Vector3f vector3f = new Vector3f((float)vector3d.x,(float)vector3d.y,(float)vector3d.z);
            return new RayResult(vector3f,new BlockPos(vector3f),side);
        } else {
            return null;
        }
    }

    public BlockState traceBlockState(float x, float y, float z, double pitch, double yaw) {
        for(int a = 0; a < distance / stepCount; a++) {
            final double Z = z - Math.sin(yaw) * Math.cos(pitch) * a * 0.1 + offSet;
            final double Y = y - Math.sin(pitch) * 0.1 * a + offSet;
            final double X = (x - Math.cos(yaw) * Math.cos(pitch) * a * 0.1) + offSet;
            BlockState blockState = getBlockState((int) Math.round(X), (int) Math.round(Y), (int) Math.round(Z));
            if(blockState.getBlock() != Blocks.AIR) {
                return blockState;
            }
        }
        return null;
    }

    public BlockPos traceBlockToBreak(double x, double y, double z, double pitch, double yaw) {
        try {
            double addX = (Math.cos(yaw) * Math.cos(pitch)) * stepCount;
            double addY = Math.sin(pitch) * stepCount;
            double addZ = Math.sin(yaw) * Math.cos(pitch) * stepCount;

            double Z = z + offSet;
            double Y = y + offSet;
            double X = x + offSet;
            for (int a = 0; a < distance / stepCount; a++) {
                Z -= addZ;
                Y -= addY;
                X -= addX;
                //final double Z = z - Math.sin(yaw) * Math.cos(pitch) * a * 0.1 + offSet;
                //final double Y = y - Math.sin(pitch) * 0.1 * a + offSet;
                //final double X = (x - Math.cos(yaw) * Math.cos(pitch) * a * 0.1) + offSet;
                BlockPos pos = new BlockPos((int) Math.round(X), (int) Math.round(Y), (int) Math.round(Z));
                BlockState blockState = getBlockState(pos);
                if (blockState.getBlock() != Blocks.AIR) {
                    if (blockState.getBlock().getBoundingBox(this, pos).intersectVector(new Vector3f((float) X - offSet, (float) Y - offSet, (float) Z - offSet), pos)) {
                        return pos;
                    }
                }
            }
        } catch (Exception ignored) {}
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

    public BlockPos getWorldSpawn(BoundingBox boundingBox) {
        BlockPos pos = new BlockPos(0,Settings.chunkHeight * 16,0);
        int y;
        out:
        for(y = 0; y < Settings.chunkHeight * 16 - 1; y++) {
            for(int z = -1; z < 2; z++) {
                for(int x = -1; x < 2; x++) {
                    BlockState blockState = getBlockState(new BlockPos(x,pos.y,z));
                    if(blockState.getBlock() != Blocks.AIR) {
                        if (blockState.getBlock().blockProperties.canWalkThrough || boundingBox.intersectsBox(blockState.getBlock().getBoundingBox(this,new BlockPos(x,pos.y,z)), pos.get3d(), new Vector3d(x, pos.y, z))) {
                            break out;
                        }
                    }
                }
            }
            pos.y -= 1;
        }
        return pos.add(0,3,0);
    }


    public static class BlockChange {
        public int x;
        public int z;
        public int y;
        public  BlockState blockState;

        public BlockChange(int x, int y, int z, BlockState blockState) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.blockState = blockState;
        }
    }
}
