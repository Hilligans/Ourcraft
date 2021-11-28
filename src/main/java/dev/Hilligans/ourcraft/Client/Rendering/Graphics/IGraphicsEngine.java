package dev.Hilligans.ourcraft.Client.Rendering.Graphics;

import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Resource.ResourceProvider;
import dev.Hilligans.ourcraft.World.Chunk;
import dev.Hilligans.ourcraft.World.ClientWorld;

public interface IGraphicsEngine<T> {

    IWindow createWindow();

    T getChunkGraphicsContainer(Chunk chunk);

    T createChunkGraphicsContainer();

    void putChunkGraphicsContainer(Chunk chunk, T container);

    void renderWorld(MatrixStack matrixStack, ClientWorld world);

    void renderScreen(MatrixStack screenStack);

    ResourceProvider createResourceProvider();

    void setup();

  //  default Runnable createRender

}
