package dev.hilligans.ourcraft.client.rendering.graphics.tasks;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.VertexMesh;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderTask;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderTaskSource;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.graphics.ShaderSource;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IDefaultEngineImpl;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.client.rendering.graphics.implementations.splitwindows.SplitWindow;
import dev.hilligans.ourcraft.client.rendering.graphics.implementations.splitwindows.SubWindow;

public class SplitWindowRenderTask extends RenderTaskSource {

    public ShaderSource shaderSource;

    public SplitWindowRenderTask() {
        super("split_window_render_task", "ourcraft:split_window_renderer");
    }

    @Override
    public RenderTask getDefaultTask() {
        return new RenderTask() {
            @Override
            public void draw(RenderWindow window, GraphicsContext graphicsContext, IGraphicsEngine<?, ?, ?> engine, Client client, MatrixStack worldStack, MatrixStack screenStack, float delta) {
                long fboOld = engine.getDefaultImpl().getBoundFBO(graphicsContext);
                SplitWindow splitWindow = (SplitWindow) window;
                for(SubWindow subWindow : splitWindow.windows) {
                    long fbo = subWindow.getTexture();
                    engine.getDefaultImpl().bindFrameBuffer(graphicsContext, fbo);
                    engine.getDefaultImpl().clearFBO(graphicsContext, subWindow.clearColor);
                    subWindow.renderPipeline(client, subWindow.camera.getMatrix(), subWindow.camera.getScreenStack(), graphicsContext);
                }

                engine.getDefaultImpl().bindFrameBuffer(graphicsContext, fboOld);

                for(SubWindow subWindow : splitWindow.windows) {
                    float x = subWindow.offsetX;
                    float y = subWindow.offsetY;
                    float minX = 0;
                    float minY = 0;
                    float maxX = 1;
                    float maxY = 1;
                    float width = subWindow.width;
                    float height = subWindow.height;
                    float[] vertices = new float[] {x,y,0,minX,minY,x,y + height,0,minX,maxY,x + width,y,0,maxX,minY,x + width,y + height,0,maxX,maxY};
                    int[] indices = new int[] {0,1,2,2,1,3};

                    VertexMesh mesh = new VertexMesh(shaderSource.vertexFormat);

                    mesh.addData(indices, vertices);

                    engine.getDefaultImpl().bindPipeline(graphicsContext, shaderSource.program);
                    engine.getDefaultImpl().bindTexture(graphicsContext, subWindow.getTexture());
                    engine.getDefaultImpl().drawAndDestroyMesh(graphicsContext, screenStack, mesh);
                    subWindow.swapBuffers(graphicsContext);
                }
            }
        };
    }

    @Override
    public void load(GameInstance gameInstance) {
        super.load(gameInstance);
        shaderSource = gameInstance.SHADERS.get("ourcraft:position_texture");
    }
}
