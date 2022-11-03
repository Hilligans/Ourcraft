package dev.Hilligans.ourcraft.Client.Rendering;

import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IDefaultEngineImpl;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.ShaderSource;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.VertexFormat;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.Image;
import dev.Hilligans.ourcraft.Client.Rendering.World.Managers.VAOManager;
import dev.Hilligans.ourcraft.Client.Rendering.World.Managers.WorldTextureManager;
import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.Resource.ResourceLocation;
import dev.Hilligans.ourcraft.Util.Registry.IRegistryElement;
import dev.Hilligans.ourcraft.Util.Settings;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Texture implements IRegistryElement {

    public String path;
    public ModContent source;

    public int width;
    public int height;

    public int textureId = -1;

    public Image texture;
    public ShaderSource shaderSource;

    public int program;

    public Texture(String path) {
        this.path = path;
    }

    public Texture(String path, Image texture) {
        this.path = path;
        width = texture.getWidth();
        height = texture.getHeight();
        this.texture = texture;
    }

    public void drawTexture(RenderWindow window, MatrixStack matrixStack, int x, int y, int width, int height, int startX, int startY, int endX, int endY) {
        IDefaultEngineImpl<?,?> defaultEngineImpl = window.getEngineImpl();

        float minX = (float)startX / this.width;
        float minY = (float)startY / this.height;
        float maxX = (float)endX / this.width;
        float maxY = (float)endY / this.height;
        float[] vertices = new float[] {x,y,-1,minX,minY,x,y + height,-1,minX,maxY,x + width,y,-1,maxX,minY,x + width,y + height,-1,maxX,maxY};
        int[] indices = new int[] {0,1,2,2,1,3};

        VertexMesh mesh = new VertexMesh(shaderSource.vertexFormat);

        mesh.addData(indices, vertices);

        //GL11.glDisable(GL11.GL_DEPTH_TEST);
        defaultEngineImpl.drawAndDestroyMesh(window,null,matrixStack,mesh,textureId,shaderSource.program);
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
    public String getIdentifierName() {
        return source.getModID() + ":" + path;
    }

    @Override
    public String getUniqueName() {
        return "texture." + source.getModID() + "." + path;
    }

    @Override
    public void assignModContent(ModContent modContent) {
        this.source = modContent;
    }

    @Override
    public void load(GameInstance gameInstance) {
        shaderSource = gameInstance.SHADERS.get("ourcraft:position_texture");
        texture = (Image) gameInstance.RESOURCE_LOADER.getResource(new ResourceLocation(path,source.getModID()));
        if(texture == null) {
            System.err.println(path);
            return;
        }
        width = texture.getWidth();
        height = texture.getHeight();
    }

    @Override
    public void loadGraphics(IGraphicsEngine<?, ?, ?> graphicsEngine) {
        IRegistryElement.super.loadGraphics(graphicsEngine);
        textureId = graphicsEngine.getDefaultImpl().createTexture(null,null, texture);
    }
}
