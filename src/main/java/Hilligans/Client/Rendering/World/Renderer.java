package Hilligans.Client.Rendering.World;

import Hilligans.Block.Block;
import Hilligans.Client.MatrixStack;
import Hilligans.ClientMain;
import Hilligans.Util.Util;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL30.*;

public class Renderer {

    public static int cursorId;

    public static int textureRenderer;


    public static void register() {
        textureRenderer = ShaderManager.registerShader(Util.imageShader,Util.imageFragment);
    }

    public static void drawTexture(int id, int width, int height) {
        int readFboId = glGenFramebuffers();
        glBindFramebuffer(GL_READ_FRAMEBUFFER,readFboId);
        glFramebufferTexture2D(GL_READ_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, id, 0);
        glBlitFramebuffer(0, 0, width, height, 0, 0, ClientMain.windowX/4, ClientMain.windowY/4, GL_COLOR_BUFFER_BIT, GL_NEAREST);
        glBindFramebuffer(GL_READ_FRAMEBUFFER, 0);
        glDeleteFramebuffers(readFboId);
    }

    public static void drawTexture1(MatrixStack matrixStack, int id, int x, int y, int width, int height) {
        int z = 0;

        float[] vertices = new float[] {x,y,z,0,0,x,y + height,z,0,1,x + width,y,z,1,0,x + width,y + height,z,1,1};
        int[] indices = new int[] {0,1,2,2,1,3};

        //GL30.glUseProgram(textureRenderer);
        glUseProgram(ClientMain.shaderProgram);
        int vao = VAOManager.createVAO(vertices, indices);

        GL30.glBindTexture(GL_TEXTURE_2D,id);
        GL30.glBindVertexArray(vao);

        matrixStack.applyTransformation();
        matrixStack.applyColor();

        // glDisable(GL_CULL_FACE);

        glDrawElements(GL_TRIANGLES, vertices.length,GL_UNSIGNED_INT,0);

        //glEnable(GL_CULL_FACE);
        VAOManager.destroyBuffer(vao);
    }

    public static void renderBlockItem(MatrixStack matrixStack, int x, int y, int size, Block block) {
        glUseProgram(ClientMain.shaderProgram);
        glDisable(GL_DEPTH_TEST);
        int vao = VAOManager.createVAO(VAOManager.getBlockVertices(block,false,size),VAOManager.getBlockIndices(block));
        matrixStack.push();
        GL30.glBindVertexArray(vao);
        glBindTexture(GL_TEXTURE_2D, ClientMain.texture);
        //matrixStack.rotate(0.785398f,new Vector3f(0,1,0));

        matrixStack.translate(x,y,0);

        //matrixStack.rotate(xAngle,new Vector3f(1,0,0));
        //matrixStack.rotate(xAngle, new Vector3f(0,(float)Math.cos(xAngle),(float)-Math.sin(xAngle)));

        matrixStack.rotate(0.785f,new Vector3f(0.5f,-1,0));
        matrixStack.rotate(0.186f,new Vector3f(0,0,-1));
        //matrixStack.rotate();
        matrixStack.translate(0,0,-size * 2);
        matrixStack.applyTransformation();
        glDrawElements(GL_TRIANGLES, 52,GL_UNSIGNED_INT,0);
        matrixStack.pop();
        VAOManager.destroyBuffer(vao);

        glEnable(GL_DEPTH_TEST);
    }

    public static void create(int id) {
        readFboid = glGenFramebuffers();
        glBindFramebuffer(GL_READ_FRAMEBUFFER, readFboid);
        glFramebufferTexture2D(GL_READ_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
                GL_TEXTURE_2D, id, 0);
        glBindFramebuffer(GL_READ_FRAMEBUFFER, 0);
    }

    static int readFboid;



}
