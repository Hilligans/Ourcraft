package Hilligans.Client.Rendering;

import Hilligans.Block.Block;
import Hilligans.Client.Client;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.World.Managers.*;
import Hilligans.ClientMain;
import Hilligans.Util.Settings;
import Hilligans.Util.Util;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL30.*;

public class Renderer {

    public static void drawTexture(MatrixStack matrixStack, int id, int x, int y, int width, int height) {
        int z = 0;

        float[] vertices = new float[] {x,y,z,0,0,x,y + height,z,0,1,x + width,y,z,1,0,x + width,y + height,z,1,1};
        int[] indices = new int[] {0,1,2,2,1,3};

        //GL30.glUseProgram(textureRenderer);
        glUseProgram(ClientMain.getClient().shaderManager.shaderProgram);
        int vao = VAOManager.createVAO(vertices, indices);
        GL30.glBindTexture(GL_TEXTURE_2D,id);
        GL30.glBindVertexArray(vao);
        matrixStack.applyTransformation();
        matrixStack.applyColor();
        glDrawElements(GL_TRIANGLES,6,GL_UNSIGNED_INT,0);
        VAOManager.destroyBuffer(vao);
    }

    public static void renderBlockItem(MatrixStack matrixStack, int x, int y, int size, Block block) {
        glUseProgram(ClientMain.getClient().shaderManager.colorShader);
        glDisable(GL_DEPTH_TEST);
        int vao = VAOManager.createColorVAO(VAOManager.getBlockVertices(block,true,size),VAOManager.getBlockIndices(block));
        matrixStack.push();
        GL30.glBindVertexArray(vao);
        glBindTexture(GL_TEXTURE_2D, ClientMain.getClient().texture);
        matrixStack.translate(x + size / 3f,y + size / 1.3f,0);
        matrixStack.rotate(0.785f,new Vector3f(0.5f,-1,0));
        matrixStack.rotate(0.186f,new Vector3f(0,0,-1));
        matrixStack.rotate(3.1415f,new Vector3f(0,0,1));
        matrixStack.translate(0,0 ,-size * 2);
        matrixStack.applyTransformation(ClientMain.getClient().shaderManager.colorShader);
        glDrawElements(GL_TRIANGLES, 36,GL_UNSIGNED_INT,0);
        matrixStack.pop();
        VAOManager.destroyBuffer(vao);
        glEnable(GL_DEPTH_TEST);
    }

    public static void renderItem(MatrixStack matrixStack, int x, int y, int size, TextureManager textureManager) {
        size *= 2;
        size -= Settings.guiSize * 2;
        x += Settings.guiSize;
        y += Settings.guiSize;
        matrixStack.applyTransformation(ClientMain.getClient().shaderManager.shaderProgram);
        glUseProgram(ClientMain.getClient().shaderManager.shaderProgram);
        glDisable(GL_DEPTH_TEST);
        int id = textureManager.getTextureId();
        float minX = WorldTextureManager.getMinX(id);
        float maxX = WorldTextureManager.getMaxX(id);
        float minY = WorldTextureManager.getMinY(id);
        float maxY = WorldTextureManager.getMaxY(id);
        float[] vertices = new float[] {x,y,0,minX,minY,x,y + size,0,minX,maxY,x + size,y,0,maxX,minY,x + size,y + size,0,maxX,maxY};
        int[] indices = new int[] {0,1,2,2,1,3};
        int vao = VAOManager.createVAO(vertices,indices);
        GL30.glBindTexture(GL_TEXTURE_2D, textureManager.getTextureMap());
        GL30.glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES,6,GL_UNSIGNED_INT,0);
        VAOManager.destroyBuffer(vao);
        glEnable(GL_DEPTH_TEST);
    }

    public static void drawTexture(MatrixStack matrixStack, Texture texture, int x, int y, int width, int height) {
        drawTexture(matrixStack, texture,x,y,width,height,0,0,texture.width,texture.height);
    }

    public static void drawTexture(MatrixStack matrixStack, Texture texture, int x, int y, int width, int height, int startX, int startY, int endX, int endY) {
        matrixStack.applyTransformation();
        float minX = (float)startX / texture.width;
        float minY = (float)startY / texture.height;
        float maxX = (float)endX / texture.width;
        float maxY = (float)endY / texture.height;
        float[] vertices = new float[] {x,y,0,minX,minY,x,y + height,0,minX,maxY,x + width,y,0,maxX,minY,x + width,y + height,0,maxX,maxY};
        int[] indices = new int[] {0,1,2,2,1,3};
        glDisable(GL_DEPTH_TEST);
        glUseProgram(ClientMain.getClient().shaderManager.shaderProgram);
        int vao = VAOManager.createVAO(vertices, indices);
        GL30.glBindTexture(GL_TEXTURE_2D,texture.textureId);
        GL30.glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, 6,GL_UNSIGNED_INT,0);
        VAOManager.destroyBuffer(vao);
        glEnable(GL_DEPTH_TEST);
    }

    public static void drawTextureAlternative(MatrixStack matrixStack, Texture texture, int x, int y, int width, int height, int startX, int startY, int endX, int endY) {
        matrixStack.applyTransformation();
        float minX = (float)startX / texture.width;
        float minY = (float)startY / texture.height;
        float maxX = (float)endX / texture.width;
        float maxY = (float)endY / texture.height;
        float[] vertices = new float[] {x + startX,y + startY,0,minX,minY,x + startX,y + endY,0,minX,maxY,x + endX,y + startY,0,maxX,minY,x + endX,y + endY,0,maxX,maxY};
        int[] indices = new int[] {0,1,2,2,1,3};
        glDisable(GL_DEPTH_TEST);
        glUseProgram(ClientMain.getClient().shaderManager.shaderProgram);
        int vao = VAOManager.createVAO(vertices, indices);
        GL30.glBindTexture(GL_TEXTURE_2D,texture.textureId);
        GL30.glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, 6,GL_UNSIGNED_INT,0);
        VAOManager.destroyBuffer(vao);
        glEnable(GL_DEPTH_TEST);
    }

    public static void drawTexture(MatrixStack matrixStack, Texture texture, int x, int y, int startX, int startY, int endX, int endY) {
        drawTexture(matrixStack,texture,x,y,(int)((endX - startX) * Settings.guiSize),(int) ((endY - startY) * Settings.guiSize),startX,startY,endX,endY);
    }

    public static void drawCenteredTexture(MatrixStack matrixStack, Texture texture,float size) {
        drawTexture(matrixStack, texture, (int)(ClientMain.getWindowX() / 2 - texture.width / 2 * size), (int)(ClientMain.getWindowY() / 2 - texture.height / 2 * size),(int)(texture.width * size), (int)(texture.height * size));
    }

    public static void drawCenteredTexture(MatrixStack matrixStack, Texture texture, int startX, int startY, int endX, int endY, float size) {
        int width = (int) ((endX - startX) * size);
        int height = (int) ((endY - startY) * size);
        drawTexture(matrixStack, texture, ClientMain.getWindowX() / 2 - width / 2,  ClientMain.getWindowY() / 2 - height / 2, width, height, startX,startY,endX,endY);
    }

    public static int drawCenteredXTexture(MatrixStack matrixStack, Texture texture, int y, float size) {
        int x = (int)(ClientMain.getWindowX() / 2 - texture.width / 2 * size);
        drawTexture(matrixStack, texture, x, y,(int)(texture.width * size), (int)(texture.height * size));
        return x;
    }

    public static int drawCenteredXTexture(MatrixStack matrixStack, Texture texture, int y, float width, float height) {
        int x = (int) (ClientMain.getWindowX() / 2 - width / 2);
        drawTexture(matrixStack, texture, x,  y, (int)width, (int)height);
        return x;
    }

    public static int drawCenteredXTexture(MatrixStack matrixStack, Texture texture, int y, int startX, int startY, int endX, int endY, float size) {
        int width = (int) ((endX - startX) * size);
        int height = (int) ((endY - startY) * size);
        int x = ClientMain.getWindowX() / 2 - width / 2;
        drawTexture(matrixStack, texture, x,  y, width, height, startX,startY,endX,endY);
        return x;
    }

    public static int drawCenteredYTexture(MatrixStack matrixStack, Texture texture, int x, float size) {
        int y = (int)(ClientMain.getWindowY() / 2 - texture.height / 2 * size);
        drawTexture(matrixStack, texture, x, y,(int)(texture.width * size), (int)(texture.height * size));
        return y;
    }

    public static int drawCenteredYTexture(MatrixStack matrixStack, Texture texture, int x, int startX, int startY, int endX, int endY, float size) {
        int width = (int) ((endX - startX) * size);
        int height = (int) ((endY - startY) * size);
        int y = ClientMain.getWindowY() / 2 - height / 2;
        drawTexture(matrixStack, texture, x,  y, width, height, startX,startY,endX,endY);
        return y;
    }

    public static void create(int id) {
        glBindFramebuffer(GL_READ_FRAMEBUFFER, glGenFramebuffers());
        glFramebufferTexture2D(GL_READ_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, id, 0);
        glBindFramebuffer(GL_READ_FRAMEBUFFER, 0);
    }

    public static void resetShader(int id) {
        Matrix4f matrix4f = new Matrix4f();
        int trans = glGetUniformLocation(id, "transform");
        float[] floats = new float[16];
        glUniformMatrix4fv(trans,false,matrix4f.get(floats));
    }



}
