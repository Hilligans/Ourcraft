package dev.hilligans.engine.client.graphics.multigl;

import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;

public class MultiGLWindow extends RenderWindow {

    public long windowID;
    public int width;
    public int height;

    public MultiGLWindow(IGraphicsEngine<?, ?, ?> graphicsEngine) {
        super(graphicsEngine);
    }

    @Override
    public long getWindowID() {
        return windowID;
    }

    @Override
    public void close() {

    }

    @Override
    public boolean shouldClose() {
        return false;
    }

    @Override
    public String getClipboardString() {
        return "";
    }

    @Override
    public void setMousePosition(int x, int y) {

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
        return false;
    }

    @Override
    public String getWindowingName() {
        return "";
    }
}
