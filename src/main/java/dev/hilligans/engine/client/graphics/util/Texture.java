package dev.hilligans.engine.client.graphics.util;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.ShaderSource;
import dev.hilligans.engine.client.graphics.api.*;
import dev.hilligans.engine.client.graphics.resource.Image;
import dev.hilligans.engine.resource.ResourceLocation;
import dev.hilligans.engine.util.UniqueResource;
import dev.hilligans.engine.util.registry.IRegistryElement;
import org.jetbrains.annotations.NotNull;

public class Texture implements IRegistryElement, IGraphicsElement {
    public static float guiSize = 4.0f;

    public final String path;
    public final String modID;

    public final UniqueResource<TextureData> data = new UniqueResource<>();

    public Texture(String path, String modID) {
        this.path = path;
        this.modID = modID;
    }

    public void drawTexture(@NotNull RenderWindow window, @NotNull GraphicsContext graphicsContext, MatrixStack matrixStack, int x, int y, int width, int height, int startX, int startY, int endX, int endY) {
        IDefaultEngineImpl<?,?,?> defaultEngineImpl = window.getEngineImpl();
        GameInstance gameInstance = window.getGameInstance();
        TextureData textureData = data.get(gameInstance);

        float imgWidth = textureData.image().getWidth();
        float imgHeight = textureData.image().getHeight();

        float minX = (float)startX / imgWidth;
        float minY = (float)startY / imgHeight;
        float maxX = (float)endX / imgWidth;
        float maxY = (float)endY / imgHeight;
        float[] vertices = new float[] {x,y,0,minX,minY,x,y + height,0,minX,maxY,x + width,y,0,maxX,minY,x + width,y + height,0,maxX,maxY};
        int[] indices = new int[] {0,1,2,2,1,3};


        IMeshBuilder builder = window.getEngineImpl().getMeshBuilder(textureData.shaderSource().vertexFormat);
        //VertexMesh mesh = new VertexMesh(shaderSource.vertexFormat);

        builder.setData(vertices, indices);

        //GL11.glDisable(GL11.GL_DEPTH_TEST);
        defaultEngineImpl.bindPipeline(graphicsContext, textureData.shaderSource().program);
        defaultEngineImpl.bindTexture( graphicsContext, getTextureId(gameInstance));
        defaultEngineImpl.drawAndDestroyMesh(graphicsContext, matrixStack, builder);
    }

    public void drawTexture(@NotNull RenderWindow window, @NotNull GraphicsContext graphicsContext, MatrixStack matrixStack, int x, int y, int width, int height) {
        drawTexture(window, graphicsContext, matrixStack,x,y,width,height,0,0,this.getWidth(window.getGameInstance()), this.getHeight(window.getGameInstance()));
    }

    public void drawTexture(@NotNull RenderWindow window, @NotNull GraphicsContext graphicsContext, MatrixStack matrixStack, int x, int y, int startX, int startY, int endX, int endY) {
        drawTexture(window, graphicsContext, matrixStack,x,y,(int)((endX - startX) * guiSize),(int) ((endY - startY) * guiSize),startX,startY,endX,endY);
    }

    public void drawCenteredTexture(@NotNull RenderWindow window, @NotNull GraphicsContext graphicsContext, MatrixStack matrixStack, float size) {
        drawTexture(window, graphicsContext, matrixStack, (int)(window.getWindowWidth() / 2 - getWidth(window.getGameInstance()) / 2 * size), (int)(window.getWindowHeight() / 2 - getHeight(window.getGameInstance()) / 2 * size),(int)(getWidth(window.getGameInstance()) * size), (int)(getHeight(window.getGameInstance()) * size));
    }

    public void drawCenteredTexture(RenderWindow window, @NotNull GraphicsContext graphicsContext, MatrixStack matrixStack, int startX, int startY, int endX, int endY, float size) {
        int width = (int) ((endX - startX) * size);
        int height = (int) ((endY - startY) * size);
        drawTexture(window, graphicsContext, matrixStack, (int) (window.getWindowWidth() / 2 - width / 2), (int) (window.getWindowHeight() / 2 - height / 2), width, height, startX,startY,endX,endY);
    }

    public int drawCenteredXTexture(@NotNull RenderWindow window, @NotNull GraphicsContext graphicsContext, MatrixStack matrixStack, int y, float size) {
        int x = (int)(window.getWindowWidth() / 2 - getWidth(window.getGameInstance()) / 2 * size);
        drawTexture(window, graphicsContext, matrixStack, x, y,(int)(getWidth(window.getGameInstance()) * size), (int)(getHeight(window.getGameInstance()) * size));
        return x;
    }

    public int drawCenteredXTexture(@NotNull RenderWindow window, @NotNull GraphicsContext graphicsContext, MatrixStack matrixStack, int y, float width, float height) {
        int x = (int) (window.getWindowWidth() / 2 - width / 2);
        drawTexture(window, graphicsContext, matrixStack, x,  y, (int)width, (int)height);
        return x;
    }

    public int drawCenteredXTexture(@NotNull RenderWindow window, @NotNull GraphicsContext graphicsContext, MatrixStack matrixStack, int y, int startX, int startY, int endX, int endY, float size) {
        int width = (int) ((endX - startX) * size);
        int height = (int) ((endY - startY) * size);
        int x = (int) (window.getWindowWidth() / 2 - width / 2);
        drawTexture(window, graphicsContext, matrixStack, x,  y, width, height, startX,startY,endX,endY);
        return x;
    }

    public int drawCenteredYTexture(@NotNull RenderWindow window, @NotNull GraphicsContext graphicsContext, MatrixStack matrixStack, Texture texture, int x, float size) {
        int y = (int)(window.getWindowHeight() / 2 - texture.getWidth(window.getGameInstance()) / 2 * size);
        drawTexture(window, graphicsContext, matrixStack, x, y,(int)(texture.getWidth(window.getGameInstance()) * size), (int)(texture.getHeight(window.getGameInstance()) * size));
        return y;
    }

    public int drawCenteredYTexture(RenderWindow window, @NotNull GraphicsContext graphicsContext, MatrixStack matrixStack, int x, int startX, int startY, int endX, int endY, float size) {
        int width = (int) ((endX - startX) * size);
        int height = (int) ((endY - startY) * size);
        int y = (int) (window.getWindowHeight() / 2 - height / 2);
        drawTexture(window, graphicsContext, matrixStack, x,  y, width, height, startX,startY,endX,endY);
        return y;
    }

    public int getWidth(GameInstance gameInstance) {
        return data.get(gameInstance).image().getWidth();
    }

    public int getHeight(GameInstance gameInstance) {
        return data.get(gameInstance).image().getHeight();
    }

    public long getTextureId(GameInstance gameInstance) {
        return data.get(gameInstance).textureID();
    }

    @Override
    public String getResourceName() {
        return path;
    }

    @Override
    public String getResourceOwner() {
        return modID;
    }

    @Override
    public String getResourceType() {
        return "texture";
    }

    @Override
    public void load(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
        if(data.get(gameInstance) == null) {
            Image texture = (Image) gameInstance.RESOURCE_LOADER.getResource(new ResourceLocation(path, modID));
            if (texture == null) {
                System.err.println(path);
                return;
            }

            ShaderSource shaderSource = gameInstance.SHADERS.get("ourcraft:position_texture");

            long textureId = graphicsEngine.getDefaultImpl().createTexture(graphicsContext, texture);
            data.add(gameInstance, new TextureData(texture, textureId, shaderSource));
        }
    }

    @Override
    public void cleanup(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
        graphicsEngine.getDefaultImpl().destroyTexture(graphicsContext, getTextureId(gameInstance));
        data.get(gameInstance).image().free();
    }

    public record TextureData(Image image, long textureID, ShaderSource shaderSource) {}
}
