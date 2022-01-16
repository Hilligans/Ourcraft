package dev.Hilligans.ourcraft.Client.Rendering;

import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.Image;
import dev.Hilligans.ourcraft.Client.Rendering.World.Managers.VAOManager;
import dev.Hilligans.ourcraft.Client.Rendering.World.Managers.WorldTextureManager;
import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.Util.Settings;
import org.lwjgl.opengl.GL30;

import java.awt.image.BufferedImage;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class Texture {

    public String path;
    public ModContent source;

    public int width;
    public int height;

    public int textureId;

    public Image texture;

    public Texture(String path) {
        this.path = path;
        Textures.TEXTURES.add(this);
    }

    public Texture(String path, Image texture) {
        this.path = path;
        width = texture.getWidth();
        height = texture.getHeight();
        this.texture = texture;
    }

    public void register() {
        Image image = WorldTextureManager.loadImage1(path, source.modID);

        if(image == null) {
            System.out.println(source + ":" + path);
        }

        width = image.getWidth();
        height = image.getHeight();
        textureId = WorldTextureManager.registerTexture(image);
    }

    public void register1() {
        textureId = WorldTextureManager.registerTexture(texture);
    }


    public void drawTexture(MatrixStack matrixStack, int x, int y, int width, int height, int startX, int startY, int endX, int endY) {
        matrixStack.applyTransformation();
        float minX = (float)startX / this.width;
        float minY = (float)startY / this.height;
        float maxX = (float)endX / this.width;
        float maxY = (float)endY / this.height;
        float[] vertices = new float[] {x,y,0,minX,minY,x,y + height,0,minX,maxY,x + width,y,0,maxX,minY,x + width,y + height,0,maxX,maxY};
        int[] indices = new int[] {0,1,2,2,1,3};
        glDisable(GL_DEPTH_TEST);
        glUseProgram(ClientMain.getClient().shaderManager.shaderProgram);
        int vao = VAOManager.createVAO(vertices, indices);
        GL30.glBindTexture(GL_TEXTURE_2D,textureId);
        GL30.glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, 6,GL_UNSIGNED_INT,0);
        VAOManager.destroyBuffer(vao);
        glEnable(GL_DEPTH_TEST);
    }

    public void drawTexture(MatrixStack matrixStack, int x, int y, int width, int height) {
        drawTexture(matrixStack,x,y,width,height,0,0,this.width,this.height);
    }

    public void drawTexture(MatrixStack matrixStack, int x, int y, int startX, int startY, int endX, int endY) {
        drawTexture(matrixStack,x,y,(int)((endX - startX) * Settings.guiSize),(int) ((endY - startY) * Settings.guiSize),startX,startY,endX,endY);
    }

    public void drawCenteredTexture(MatrixStack matrixStack,float size) {
        drawTexture(matrixStack, (int)(ClientMain.getWindowX() / 2 - width / 2 * size), (int)(ClientMain.getWindowY() / 2 - height / 2 * size),(int)(width * size), (int)(height * size));
    }

    public void drawCenteredTexture(MatrixStack matrixStack, int startX, int startY, int endX, int endY, float size) {
        int width = (int) ((endX - startX) * size);
        int height = (int) ((endY - startY) * size);
        drawTexture(matrixStack, ClientMain.getWindowX() / 2 - width / 2,  ClientMain.getWindowY() / 2 - height / 2, width, height, startX,startY,endX,endY);
    }

    public int drawCenteredXTexture(MatrixStack matrixStack, int y, float size) {
        int x = (int)(ClientMain.getWindowX() / 2 - width / 2 * size);
        drawTexture(matrixStack, x, y,(int)(width * size), (int)(height * size));
        return x;
    }

    public int drawCenteredXTexture(MatrixStack matrixStack, int y, float width, float height) {
        int x = (int) (ClientMain.getWindowX() / 2 - width / 2);
        drawTexture(matrixStack, x,  y, (int)width, (int)height);
        return x;
    }

    public int drawCenteredXTexture(MatrixStack matrixStack, int y, int startX, int startY, int endX, int endY, float size) {
        int width = (int) ((endX - startX) * size);
        int height = (int) ((endY - startY) * size);
        int x = ClientMain.getWindowX() / 2 - width / 2;
        drawTexture(matrixStack, x,  y, width, height, startX,startY,endX,endY);
        return x;
    }

    public int drawCenteredYTexture(MatrixStack matrixStack, Texture texture, int x, float size) {
        int y = (int)(ClientMain.getWindowY() / 2 - texture.height / 2 * size);
        drawTexture(matrixStack, x, y,(int)(texture.width * size), (int)(texture.height * size));
        return y;
    }

    public int drawCenteredYTexture(MatrixStack matrixStack, int x, int startX, int startY, int endX, int endY, float size) {
        int width = (int) ((endX - startX) * size);
        int height = (int) ((endY - startY) * size);
        int y = ClientMain.getWindowY() / 2 - height / 2;
        drawTexture(matrixStack, x,  y, width, height, startX,startY,endX,endY);
        return y;
    }


}
