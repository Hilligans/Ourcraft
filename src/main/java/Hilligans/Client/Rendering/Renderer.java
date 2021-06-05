package Hilligans.Client.Rendering;

import Hilligans.Block.Block;
import Hilligans.Client.Client;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.NewRenderer.PrimitiveBuilder;
import Hilligans.Client.Rendering.World.Managers.*;
import Hilligans.ClientMain;
import Hilligans.Data.Other.BlockPos;
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
        PrimitiveBuilder primitiveBuilder = new PrimitiveBuilder(GL_TRIANGLES,ShaderManager.worldShader);
        for(int z = 0; z < 6; z++) {
            block.addVertices(primitiveBuilder,z,size,block.getDefaultState(),new BlockPos(0,0,0),0,0);
        }
        int verticesCount = primitiveBuilder.indices.size();
        int vao = VAOManager.createVAO(primitiveBuilder);
        matrixStack.push();
        GL30.glBindVertexArray(vao);
        glBindTexture(GL_TEXTURE_2D, ClientMain.getClient().texture);
        matrixStack.translate(x + size / 3f,y + size / 1.3f,0);
        matrixStack.rotate(0.785f,new Vector3f(0.5f,-1,0));
        matrixStack.rotate(0.186f,new Vector3f(0,0,-1));
        matrixStack.rotate(3.1415f,new Vector3f(0,0,1));
        matrixStack.translate(0,0 ,-size * 2);
        matrixStack.applyTransformation(ClientMain.getClient().shaderManager.colorShader);
        glDrawElements(GL_TRIANGLES, verticesCount,GL_UNSIGNED_INT,0);
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
