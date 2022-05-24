package dev.Hilligans.ourcraft.Client.Rendering;

import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IDefaultEngineImpl;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.VertexFormat;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.Image;
import dev.Hilligans.ourcraft.Client.Rendering.World.Managers.VAOManager;
import dev.Hilligans.ourcraft.Client.Rendering.World.Managers.WorldTextureManager;
import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.Util.Registry.IRegistryElement;
import dev.Hilligans.ourcraft.Util.Settings;

public class Texture implements IRegistryElement {

    public String path;
    public ModContent source;

    public int width;
    public int height;

    public int textureId = -1;

    public Image texture;
    public VertexFormat format;

    public Texture(String path) {
        this.path = path;
    }

    public Texture(String path, Image texture) {
        this.path = path;
        width = texture.getWidth();
        height = texture.getHeight();
        this.texture = texture;
    }

    public void register() {
        Image image = WorldTextureManager.loadImage1(path, source.getModID(), source.gameInstance);

        if(image == null) {
            System.out.println(source.getModID() + ":" + path);
            return;
        }

        width = image.getWidth();
        height = image.getHeight();
        this.texture = image;
        //textureId = WorldTextureManager.registerTexture(image);
    }

    public void drawTexture(RenderWindow window, MatrixStack matrixStack, int x, int y, int width, int height, int startX, int startY, int endX, int endY) {
        IDefaultEngineImpl<?> defaultEngineImpl = window.getEngineImpl();
        if(textureId == -1) {
            textureId = window.getEngineImpl().createTexture(window,texture);
        }

        float minX = (float)startX / this.width;
        float minY = (float)startY / this.height;
        float maxX = (float)endX / this.width;
        float maxY = (float)endY / this.height;
        float[] vertices = new float[] {x,y,0,minX,minY,x,y + height,0,minX,maxY,x + width,y,0,maxX,minY,x + width,y + height,0,maxX,maxY};
        int[] indices = new int[] {0,1,2,2,1,3};

        VertexMesh mesh = new VertexMesh(format);

        mesh.addData(indices, vertices);

        defaultEngineImpl.drawAndDestroyMesh(window,matrixStack,mesh,textureId,ClientMain.getClient().shaderManager.shaderProgram);

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
        format = gameInstance.VERTEX_FORMATS.get("ourcraft:position_texture");
        register();
    }
}
