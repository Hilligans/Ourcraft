package dev.hilligans.ourcraft.world.newworldsystem;


import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.entity.Entity;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * the main world system used for world in world worlds
 */
public class CompositeWorld implements IWorld {

    public final int id;
    public final String name;
    public final GameInstance gameInstance;

    public final ArrayList<IWorld> worlds = new ArrayList<>();
    public final ConcurrentLinkedQueue<IWorld> queuedAdditions = new ConcurrentLinkedQueue<>();
    public final ConcurrentLinkedQueue<IWorld> queuedRemovals = new ConcurrentLinkedQueue<>();

    public CompositeWorld(GameInstance gameInstance, int id, String name) {
        this.gameInstance = gameInstance;
        this.id = id;
        this.name = name;
    }

    public void addWorld(IWorld world) {
        queuedAdditions.add(world);
    }

    public void removeWorld(IWorld world) {
        queuedRemovals.add(world);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void tick() {
        while(queuedAdditions.peek() != null) {
            worlds.add(queuedAdditions.poll());
        }

        while(queuedRemovals.peek() != null) {
            worlds.remove(queuedRemovals.poll());
        }
    }

    @Override
    public IChunk getChunk(long blockX, long blockY, long blockZ) {
        return null;
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
        return gameInstance;
    }
}
