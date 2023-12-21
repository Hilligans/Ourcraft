package dev.hilligans.ourcraft.client.rendering;

import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IDefaultEngineImpl;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsElement;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.graphics.ShaderSource;
import dev.hilligans.ourcraft.client.rendering.newrenderer.Image;
import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import dev.hilligans.ourcraft.resource.ResourceLocation;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;
import dev.hilligans.ourcraft.util.Settings;

public class Texture implements IRegistryElement, IGraphicsElement {

    public String path;
    public ModContent source;

    public int width;
    public int height;

    public long textureId = -1;

    public Image texture;
    public ShaderSource shaderSource;

    public int program;

    public Texture(String path) {
        this.path = path;
    }
    

    public void drawTexture(RenderWindow window, MatrixStack matrixStack, int x, int y, int width, int height, int startX, int startY, int endX, int endY) {
        IDefaultEngineImpl<?,?> defaultEngineImpl = window.getEngineImpl();

        float minX = (float)startX / this.width;
        float minY = (float)startY / this.height;
        float maxX = (float)endX / this.width;
        float maxY = (float)endY / this.height;
        float[] vertices = new float[] {x,y,0,minX,minY,x,y + height,0,minX,maxY,x + width,y,0,maxX,minY,x + width,y + height,0,maxX,maxY};
        int[] indices = new int[] {0,1,2,2,1,3};

        VertexMesh mesh = new VertexMesh(shaderSource.vertexFormat);

        mesh.addData(indices, vertices);

        //GL11.glDisable(GL11.GL_DEPTH_TEST);
        defaultEngineImpl.bindPipeline(null, shaderSource.program);
        defaultEngineImpl.bindTexture( null, textureId);
        defaultEngineImpl.drawAndDestroyMesh(null,matrixStack,mesh);
    }

    public void drawTexture(RenderWindow window, MatrixStack matrixStack, int x, int y, int width, int height) {
        drawTexture(window, matrixStack,x,y,width,height,0,0,this.width,this.height);
    }

    public void drawTexture(RenderWindow window, MatrixStack matrixStack, int x, int y, int startX, int startY, int endX, int endY) {
        drawTexture(window, matrixStack,x,y,(int)((endX - startX) * Settings.guiSize),(int) ((endY - startY) * Settings.guiSize),startX,startY,endX,endY);
    }

    public void drawCenteredTexture(RenderWindow window, MatrixStack matrixStack, float size) {
        drawTexture(window, matrixStack, (int)(window.getWindowWidth() / 2 - width / 2 * size), (int)(window.getWindowHeight() / 2 - height / 2 * size),(int)(width * size), (int)(height * size));
    }

    public void drawCenteredTexture(RenderWindow window, MatrixStack matrixStack, int startX, int startY, int endX, int endY, float size) {
        int width = (int) ((endX - startX) * size);
        int height = (int) ((endY - startY) * size);
        drawTexture(window, matrixStack, (int) (window.getWindowWidth() / 2 - width / 2), (int) (window.getWindowHeight() / 2 - height / 2), width, height, startX,startY,endX,endY);
    }

    public int drawCenteredXTexture(RenderWindow window, MatrixStack matrixStack, int y, float size) {
        int x = (int)(window.getWindowWidth() / 2 - width / 2 * size);
        drawTexture(window, matrixStack, x, y,(int)(width * size), (int)(height * size));
        return x;
    }

    public int drawCenteredXTexture(RenderWindow window, MatrixStack matrixStack, int y, float width, float height) {
        int x = (int) (window.getWindowWidth() / 2 - width / 2);
        drawTexture(window, matrixStack, x,  y, (int)width, (int)height);
        return x;
    }

    public int drawCenteredXTexture(RenderWindow window, MatrixStack matrixStack, int y, int startX, int startY, int endX, int endY, float size) {
        int width = (int) ((endX - startX) * size);
        int height = (int) ((endY - startY) * size);
        int x = (int) (window.getWindowWidth() / 2 - width / 2);
        drawTexture(window, matrixStack, x,  y, width, height, startX,startY,endX,endY);
        return x;
    }

    public int drawCenteredYTexture(RenderWindow window, MatrixStack matrixStack, Texture texture, int x, float size) {
        int y = (int)(window.getWindowHeight() / 2 - texture.height / 2 * size);
        drawTexture(window, matrixStack, x, y,(int)(texture.width * size), (int)(texture.height * size));
        return y;
    }

    public int drawCenteredYTexture(RenderWindow window, MatrixStack matrixStack, int x, int startX, int startY, int endX, int endY, float size) {
        int width = (int) ((endX - startX) * size);
        int height = (int) ((endY - startY) * size);
        int y = (int) (window.getWindowHeight() / 2 - height / 2);
        drawTexture(window, matrixStack, x,  y, width, height, startX,startY,endX,endY);
        return y;
    }

    @Override
    public String getResourceName() {
        return path;
    }

    @Override
    public String getResourceOwner() {
        return source.getModID();
    }

    @Override
    public String getResourceType() {
        return "texture";
    }
    @Override
    public void assignModContent(ModContent modContent) {
        this.source = modContent;
    }

    @Override
    public void load(GameInstance gameInstance) {
        shaderSource = gameInstance.SHADERS.get("ourcraft:position_texture");
    }

    @Override
    public void load(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
        texture = (Image) gameInstance.RESOURCE_LOADER.getResource(new ResourceLocation(path,source.getModID()));
        if(texture == null) {
            System.err.println(path);
            return;
        }
        width = texture.getWidth();
        height = texture.getHeight();

        textureId = graphicsEngine.getDefaultImpl().createTexture(graphicsContext, texture);
    }

    @Override
    public void cleanup(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
        graphicsEngine.getDefaultImpl().destroyTexture(graphicsContext, textureId);
    }
}
