package dev.hilligans.ourcraft.Client.Rendering.Graphics.Tasks;

import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.GraphicsContext;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderTask;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderTaskSource;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.ShaderSource;
import dev.hilligans.ourcraft.GameInstance;

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
