package dev.hilligans.ourcraft.world.newworldsystem;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.ourcraft.entity.Entity;
import dev.hilligans.ourcraft.entity.IEntity;
import dev.hilligans.ourcraft.entity.IPlayerEntity;

import java.util.function.Consumer;

public class NewClientWorld implements IWorld {

    public String name;
    public int worldID;
    IThreeDChunkContainer chunkContainer = new NewChunkContainer();


    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getID() {
        return worldID;
    }

    @Override
    public void tick() {

    }

    @Override
    public IChunk getChunk(long blockX, long blockY, long blockZ) {
        return chunkContainer.getChunk(blockX,blockY,blockZ);
    }

    @Override
    public IChunk getChunkNonNull(long blockX, long blockY, long blockZ) {
        return null;
    }

    @Override
    public void setChunk(long blockX, long blockY, long blockZ, IChunk chunk) {

    }

    @Override
    public int getChunkWidth() {
        return 0;
    }

    @Override
    public int getChunkHeight() {
        return 0;
    }

    @Override
    public IThreeDChunkContainer getChunkContainer() {
        return null;
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

    }

    @Override
    public void removeEntity(IEntity entity) {

    }

    @Override
    public void forEachEntity(Consumer<IEntity> consumer) {

    }

    @Override
    public void forEachPlayer(Consumer<IPlayerEntity> consumer) {

    }
}
