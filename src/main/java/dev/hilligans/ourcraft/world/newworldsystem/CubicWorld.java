package dev.hilligans.ourcraft.world.newworldsystem;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.ourcraft.entity.Entity;
import dev.hilligans.engine.entity.IEntity;
import dev.hilligans.ourcraft.entity.IPlayerEntity;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.function.Consumer;

public class CubicWorld implements IWorld {

    public int radius;
    public String worldName;
    public int id;
    public GameInstance gameInstance;
    public final IThreeDChunkContainer chunkContainer = new CubicChunkContainer(32, 32);
    public Int2ObjectOpenHashMap<Entity> entities = new Int2ObjectOpenHashMap<>();

    public CubicWorld(GameInstance gameInstance, int id, String worldName, int radius) {
        this.gameInstance = gameInstance;
        this.radius = radius;
        this.worldName = worldName;
        this.id = id;
    }

    @Override
    public String getName() {
        return worldName;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void tick() {
    }

    //@Override
    public Vector3fc getGravityVector(Vector3f position) {
        int component = position.maxComponent();
        return new Vector3f().setComponent(component, position.get(component) > 0 ? -1 : 1);
        }

    @Override
    public IChunk getChunk(long blockX, long blockY, long blockZ) {
        return chunkContainer.getChunk(blockX >> 5, blockY >> 5, blockZ >> 5);
    }

    @Override
    public IChunk getChunkNonNull(long blockX, long blockY, long blockZ) {
        return getChunk(blockX, blockY, blockZ);
    }

    @Override
    public void setChunk(long blockX, long blockY, long blockZ, IChunk chunk) {
        chunk.setDirty(true);
        chunkContainer.setChunk(blockX >> 5, blockY >> 5, blockZ >> 5, chunk);
    }

    @Override
    public int getChunkWidth() {
        return 32;
    }

    @Override
    public int getChunkHeight() {
        return 32;
    }

    @Override
    public IThreeDChunkContainer getChunkContainer() {
        return chunkContainer;
    }

    @Override
    public void addEntity(Entity entity) {
        entities.put(entity.id, entity);
    }

    @Override
    public Entity removeEntity(long l1, long l2) {
        return entities.remove((int)l1);
    }

    @Override
    public GameInstance getGameInstance() {
        return gameInstance;
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
