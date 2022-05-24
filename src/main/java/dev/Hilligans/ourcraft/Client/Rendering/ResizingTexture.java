package dev.Hilligans.ourcraft.Client.Rendering;

import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.PrimitiveBuilder;
import dev.Hilligans.ourcraft.Client.Rendering.World.Managers.ShaderManager;
import dev.Hilligans.ourcraft.Client.Rendering.World.Managers.VAOManager;
import dev.Hilligans.ourcraft.ClientMain;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;

public class ResizingTexture extends Texture {

    public Segment startSegment;
    public Segment middleSegment;
    public Segment endSegment;
    public boolean canStretch;


    public ResizingTexture(String path) {
        this(path,true);
    }

    public ResizingTexture(String path, boolean canStretch) {
        super(path);
        this.canStretch = canStretch;
    }



    public ResizingTexture startSegment(int x, int y, int width, int height) {
        startSegment = new Segment(x,y,width,height);
        return this;
    }

    public ResizingTexture middleSegment(int x, int y, int width, int height) {
        middleSegment = new Segment(x,y,width,height);
        return this;
    }

    public ResizingTexture endSegment(int x, int y, int width, int height) {
        endSegment = new Segment(x,y,width,height);
        return this;
    }

    public int createVAO(int width, int height,int x, int y) {
        PrimitiveBuilder primitiveBuilder = new PrimitiveBuilder(GL_TRIANGLES, ShaderManager.guiShader);
        startSegment.put(primitiveBuilder,x,y,height * startSegment.getRatio(),height,this);
        float middleWidth = width - height * startSegment.getRatio() - height * endSegment.getRatio();
        middleSegment.put(primitiveBuilder,height * startSegment.getRatio() + x,y,middleWidth,height,this);
        endSegment.put(primitiveBuilder,height * startSegment.getRatio() + middleWidth + x,y,height * endSegment.getRatio(),height,this);
        return VAOManager.createVAO(primitiveBuilder);
    }

    public void drawTexture(MatrixStack matrixStack, int x, int y, int width, int height) {
        matrixStack.push();
        glDisable(GL_DEPTH_TEST);
        //glUseProgram(ClientMain.getClient().shaderManager.shaderProgram);
        int vao = createVAO(width,height,x,y);
        GL30.glBindTexture(GL_TEXTURE_2D,textureId);
        GL30.glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, 18,GL_UNSIGNED_INT,0);
        VAOManager.destroyBuffer(vao);
        glEnable(GL_DEPTH_TEST);
        matrixStack.pop();
    }



    static class Segment {

        public int x;
        public int y;
        public int width;
        public int height;

        public Segment(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public void put(PrimitiveBuilder builder, float x, float y, float width, float height, ResizingTexture resizingTexture) {
            builder.buildQuad(x,y,0,1f/resizingTexture.width * this.x,1f/resizingTexture.height * this.y,x + width, y + height, 1f/ resizingTexture.width * (this.x + this.width),1f/resizingTexture.height *  (this.y + this.height));
        }

        public float getRatio() {
            return (float)width / height;
        }
    }
}
