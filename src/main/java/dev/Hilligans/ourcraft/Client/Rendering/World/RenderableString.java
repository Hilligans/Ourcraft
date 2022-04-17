package dev.Hilligans.ourcraft.Client.Rendering.World;

import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.World.Managers.VAOManager;
import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.Util.Vector5f;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class RenderableString {

    public int id;
    public int size;
    public int length;
    public int height;
    public int vertexCount;

    public RenderableString(int id, int size, int length) {
        this.id = id;
        this.size = size;
        this.length = length;
    }
    /*

    public RenderableString(int id, int size, int length, int height) {
        this(id,size,length);
        this.height = height;
    }

    public RenderableString(String string, int x, int y, float scale) {
        height = StringRenderer.instance.stringHeight;
        ArrayList<Vector5f> vector5fs = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();
        for(int z = 0; z < string.length(); z++) {
            //vector5fs.addAll(Arrays.asList(StringRenderer.getVertices("" + string.charAt(z),x + length ,y,scale)));
             length += StringRenderer.instance.characterOffset.get("" + string.charAt(z)).getTypeA() * scale;
            //indices.addAll(Arrays.asList(StringRenderer.getIndices(z * 4)));
        }
        id = VAOManager.createVAO(VAOManager.convertVertices(vector5fs,false),VAOManager.convertIndices(indices));
        vertexCount = vector5fs.size() * 5;
    }

    public RenderableString(String string, float scale) {
        height = StringRenderer.instance.stringHeight;
        ArrayList<Vector5f> vector5fs = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();
        for(int z = 0; z < string.length(); z++) {
            //vector5fs.addAll(Arrays.asList(StringRenderer.getVertices("" + string.charAt(z), length ,0,scale)));
            length += StringRenderer.instance.characterOffset.get("" + string.charAt(z)).getTypeA() * scale;
            //indices.addAll(Arrays.asList(StringRenderer.getIndices(z * 4)));
        }
        id = VAOManager.createVAO(VAOManager.convertVertices(vector5fs,false),VAOManager.convertIndices(indices));
        vertexCount = vector5fs.size() * 5;
    }



    public void draw(MatrixStack matrixStack, int x, int y) {
        glDisable(GL_DEPTH_TEST);
        matrixStack.push();
        matrixStack.translate(x,y,0);
        glUseProgram(ClientMain.getClient().shaderManager.colorShader);
        matrixStack.applyColor(ClientMain.getClient().shaderManager.colorShader);
        matrixStack.applyTransformation(ClientMain.getClient().shaderManager.colorShader);
        draw(id, vertexCount);
        matrixStack.pop();
        glEnable(GL_DEPTH_TEST);
    }

    public void draw(MatrixStack matrixStack) {
        glDisable(GL_DEPTH_TEST);
        matrixStack.push();
        glUseProgram(ClientMain.getClient().shaderManager.colorShader);
        matrixStack.applyColor(ClientMain.getClient().shaderManager.colorShader);
        matrixStack.applyTransformation(ClientMain.getClient().shaderManager.colorShader);
        draw(id, vertexCount);
        matrixStack.pop();
        glEnable(GL_DEPTH_TEST);
    }

    public static void draw(int id, int size) {
        GL30.glBindTexture(GL_TEXTURE_2D,StringRenderer.instance.mappedCharacters);
        GL30.glBindVertexArray(id);
        glDrawElements(GL_TRIANGLES, size,GL_UNSIGNED_INT,0);
        //VAOManager.destroyBuffer(id);
    }

    public void destroy() {
        //System.out.println(id);
        VAOManager.destroyBuffer(id);
    }

    */



}
