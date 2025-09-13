package dev.hilligans.ourcraft.client.rendering.culling;

import dev.hilligans.engine.client.graphics.api.ICamera;
import dev.hilligans.ourcraft.world.newworldsystem.IChunk;
import dev.hilligans.ourcraft.world.newworldsystem.IWorld;

public abstract class CullingEngine {

    IWorld world;

    public CullingEngine(IWorld world) {
        this.world = world;
    }

    public abstract boolean shouldRenderChunk(IChunk chunk, ICamera camera);

}
