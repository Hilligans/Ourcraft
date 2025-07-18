package dev.hilligans.ourcraft.world.newworldsystem;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.block.blockstate.IBlockState;
import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.entity.Entity;
import dev.hilligans.ourcraft.entity.IEntity;
import dev.hilligans.ourcraft.entity.IPlayerEntity;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

public class SimpleWorld implements IWorld {

    public int worldID;
    public String worldName;
    public boolean randomTick = false;
    public boolean isClientWorld = true;

    public float randomTickSpeed = 3f / (16*16*16);

    public IThreeDChunkContainer chunkContainer;

    public ArrayList<IPlayerEntity> players = new ArrayList<>();
    public ArrayList<IEntity> entities = new ArrayList<>();

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
                        //blockState.getBlock().randomTick(self, blockState, self, pos, random);
                    }
                }
            });
        }

    }

    @Override
    public IChunk getChunk(long blockX, long blockY, long blockZ) {
        return chunkContainer.getChunk(blockX >> 4, 0, blockZ >> 4);
    }

    @Override
    public IChunk getChunkNonNull(long blockX, long blockY, long blockZ) {
        return getChunk(blockX,blockY,blockZ);
    }

    @Override
    public void setChunk(long blockX, long blockY, long blockZ, IChunk chunk) {
        IChunk oldChunk = chunkContainer.setChunk(blockX,blockY,blockZ,chunk);
        chunk.set(this);
        if(isClientWorld) {
            chunk.setDirty(true);
        }
        if(oldChunk != null) {
            oldChunk.free(this);
        }
    }

    @Override
    public int getChunkWidth() {
        return 16;
    }

    @Override
    public int getChunkHeight() {
        return 256;
    }

    @Override
    public IThreeDChunkContainer getChunkContainer() {
        return chunkContainer;
    }

    @Override
    public void addEntity(Entity entity) {

    }

    @Override
    public Entity removeEntity(long l1, long l2) {
        return null;
    }

    @Override
    public GameInstance getGameInstance() {
        return null;
    }

    @Override
    public void addEntity(IEntity entity) {
        if(entity instanceof IPlayerEntity) {
            players.add((IPlayerEntity) entity);
        }
        entities.add(entity);
    }

    @Override
    public void removeEntity(IEntity entity) {
        entities.remove(entity);
        if(entity instanceof IPlayerEntity) {
            players.remove((IPlayerEntity) entity);
        }
    }

    @Override
    public void forEachEntity(Consumer<IEntity> consumer) {
        entities.forEach(consumer);
    }

    @Override
    public void forEachPlayer(Consumer<IPlayerEntity> consumer) {
        players.forEach(consumer);
    }
}
