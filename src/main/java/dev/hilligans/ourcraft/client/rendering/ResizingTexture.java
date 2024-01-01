package dev.hilligans.ourcraft.client.rendering;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IDefaultEngineImpl;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.newrenderer.PrimitiveBuilder;
import org.jetbrains.annotations.NotNull;

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

    public PrimitiveBuilder get(GameInstance gameInstance, int width, int height, int x, int y) {
        PrimitiveBuilder primitiveBuilder = new PrimitiveBuilder(shaderSource.vertexFormat);
        startSegment.put(primitiveBuilder,x,y,height * startSegment.getRatio(),height,this.getWidth(gameInstance), this.getHeight(gameInstance));
        float middleWidth = width - height * startSegment.getRatio() - height * endSegment.getRatio();
        middleSegment.put(primitiveBuilder,height * startSegment.getRatio() + x,y,middleWidth,height,this.getWidth(gameInstance), this.getHeight(gameInstance));
        endSegment.put(primitiveBuilder,height * startSegment.getRatio() + middleWidth + x,y,height * endSegment.getRatio(),height,this.getWidth(gameInstance), this.getHeight(gameInstance));
        return primitiveBuilder;
    }


    @Override
    public void drawTexture(RenderWindow window, @NotNull GraphicsContext graphicsContext, MatrixStack matrixStack, int x, int y, int width, int height) {
        PrimitiveBuilder primitiveBuilder = get(window.getGameInstance(), width,height,x,y);
        IDefaultEngineImpl<?,?> defaultEngineImpl = window.getEngineImpl();
        defaultEngineImpl.bindTexture(graphicsContext, getTextureId(window.getGameInstance()));
        defaultEngineImpl.bindPipeline(graphicsContext, shaderSource.program);
        defaultEngineImpl.uploadMatrix(graphicsContext, matrixStack, shaderSource);
        defaultEngineImpl.drawAndDestroyMesh(graphicsContext,matrixStack,primitiveBuilder.toVertexMesh());
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

        public void put(PrimitiveBuilder builder, float x, float y, float width, float height, int textureWidth, int textureHeight) {
            builder.buildQuad(x,y,0,1f/textureWidth * this.x,1f/textureHeight * this.y,x + width, y + height, 1f/ textureWidth * (this.x + this.width),1f/textureHeight *  (this.y + this.height));
        }

        public float getRatio() {
            return (float)width / height;
        }
    }
}
