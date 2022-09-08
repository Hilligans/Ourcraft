package dev.Hilligans.ourcraft.Client.Rendering.Culling;

import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.ICamera;
import dev.Hilligans.ourcraft.World.Chunk;
import dev.Hilligans.ourcraft.World.NewWorldSystem.IWorld;

public abstract class CullingEngine {

    IWorld world;

    public CullingEngine(IWorld world) {
        this.world = world;
    }

    public abstract boolean shouldRenderChunk(Chunk chunk, ICamera camera);

}
