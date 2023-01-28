package dev.hilligans.ourcraft.Client.Rendering;

import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.IDefaultEngineImpl;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.hilligans.ourcraft.Client.Rendering.NewRenderer.PrimitiveBuilder;

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

    public PrimitiveBuilder get(int width, int height,int x, int y) {
        PrimitiveBuilder primitiveBuilder = new PrimitiveBuilder(shaderSource.vertexFormat);
        startSegment.put(primitiveBuilder,x,y,height * startSegment.getRatio(),height,this);
        float middleWidth = width - height * startSegment.getRatio() - height * endSegment.getRatio();
        middleSegment.put(primitiveBuilder,height * startSegment.getRatio() + x,y,middleWidth,height,this);
        endSegment.put(primitiveBuilder,height * startSegment.getRatio() + middleWidth + x,y,height * endSegment.getRatio(),height,this);
        return primitiveBuilder;
    }


    @Override
    public void drawTexture(RenderWindow window, MatrixStack matrixStack, int x, int y, int width, int height) {
        PrimitiveBuilder primitiveBuilder = get(width,height,x,y);
        IDefaultEngineImpl<?,?> defaultEngineImpl = window.getEngineImpl();
        defaultEngineImpl.uploadMatrix(null, matrixStack, shaderSource);
        defaultEngineImpl.drawAndDestroyMesh(window,null,matrixStack,primitiveBuilder.toVertexMesh(),textureId,shaderSource.program);
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
