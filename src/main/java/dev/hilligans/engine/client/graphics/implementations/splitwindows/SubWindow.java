package dev.hilligans.engine.client.graphics.implementations.splitwindows;

import dev.hilligans.engine.application.IClientApplication;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;

public class SubWindow extends RenderWindow {

    public SplitWindow parent;
    public int width;
    public int height;

    public int offsetX;
    public int offsetY;

    public long fbo1, fbo2;

    public SubWindow(IGraphicsEngine<?, ?, ?> graphicsEngine, SplitWindow parent, int width, int height, GraphicsContext graphicsContext) {
        super(graphicsEngine);
        this.parent = parent;
        this.width = width;
        this.height = height;

        fbo1 = graphicsEngine.getDefaultImpl().createFrameBuffer(graphicsContext, width, height);
        fbo2 = graphicsEngine.getDefaultImpl().createFrameBuffer(graphicsContext, width, height);
    }

    public long getTexture() {
        return fbo1;
    }

    @Override
    public void swapBuffers(GraphicsContext graphicsContext) {
        super.swapBuffers(graphicsContext);
        long t = fbo1;
        fbo1 = fbo2;
        fbo2 = t;
    }

    @Override
    public long getWindowID() {
        return 0;
    }

    @Override
    public void close() {

    }

    @Override
    public boolean shouldClose() {
        return parent.shouldClose();
    }

    @Override
    public IClientApplication getClient() {
        return parent.getClient();
    }

    @Override
    public String getClipboardString() {
        return parent.getClipboardString();
    }

    @Override
    public void setMousePosition(int x, int y) {
        parent.setMousePosition(x + offsetX, y + offsetY);
    }

    @Override
    public int getWindowWidth() {
        return width;
    }

    @Override
    public int getWindowHeight() {
        return height;
    }

    @Override
    public boolean isWindowFocused() {
        return parent.isWindowFocused();
    }

    @Override
    public String getWindowingName() {
        return parent.getWindowingName();
    }
}
