package dev.Hilligans.ourcraft.Client.Rendering.Graphics;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;

public class SplitWindow extends RenderWindow {

    public int windowCountX;
    public int windowCountY;

    public SplitWindow(IGraphicsEngine<?, ?, ?> graphicsEngine) {
        super(graphicsEngine);
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
        return false;
    }

    @Override
    public void swapBuffers() {

    }

    @Override
    public Client getClient() {
        return null;
    }

    @Override
    public float getWindowWidth() {
        return 0;
    }

    @Override
    public float getWindowHeight() {
        return 0;
    }

    @Override
    public boolean isWindowFocused() {
        return false;
    }

    @Override
    public String getWindowingName() {
        return null;
    }
}
