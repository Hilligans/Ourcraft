package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Tasks;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.GraphicsContext;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderTask;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderTaskSource;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.ShaderSource;
import dev.Hilligans.ourcraft.GameInstance;

public class ChunkDebugRenderTask extends RenderTaskSource {

    public ShaderSource shaderSource;

    public ChunkDebugRenderTask() {
        super("", "");
    }

    @Override
    public RenderTask getDefaultTask() {
        return new RenderTask() {
            @Override
            public void draw(RenderWindow window, GraphicsContext graphicsContext, IGraphicsEngine<?, ?, ?> engine, Client client, MatrixStack worldStack, MatrixStack screenStack, float delta) {

            }
        };
    }

    @Override
    public void load(GameInstance gameInstance) {
        super.load(gameInstance);
        shaderSource = gameInstance.SHADERS.get("ourcraft:position_color_shader");
    }
}
