package dev.Hilligans.ourcraft.Client.Rendering.Graphics;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.GraphicsContext;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IDefaultEngineImpl;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;

public abstract class RenderTask {

    public abstract void draw(RenderWindow window, GraphicsContext graphicsContext, IGraphicsEngine<?, ?, ?> engine, Client client, MatrixStack worldStack, MatrixStack screenStack, float delta);

}
