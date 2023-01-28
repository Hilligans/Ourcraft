package dev.hilligans.ourcraft.Client.Rendering.Culling;

import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.ICamera;
import dev.hilligans.ourcraft.World.Chunk;
import dev.hilligans.ourcraft.World.NewWorldSystem.IWorld;

public abstract class CullingEngine {

    IWorld world;

    public CullingEngine(IWorld world) {
        this.world = world;
    }

    public abstract boolean shouldRenderChunk(Chunk chunk, ICamera camera);

}
