package dev.hilligans.ourcraft.client.rendering.graphics.implementations.splitwindows;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;

import java.util.ArrayList;

public class FloatingWindow extends RenderWindow {

    public ArrayList<RenderWindow> windows = new ArrayList<>();


    public FloatingWindow(IGraphicsEngine<?,?,?> graphicsEngine) {
        super(graphicsEngine);
    }

    public void addWindow(RenderWindow window) {
        this.windows.add(window);
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
    public void swapBuffers(GraphicsContext graphicsContext) {

    }

    @Override
    public Client getClient() {
        return null;
    }

    @Override
    public String getClipboardString() {
        return null;
    }

    @Override
    public void setMousePosition(int x, int y) {

    }

    @Override
    public int getWindowWidth() {
        return 0;
    }

    @Override
    public int getWindowHeight() {
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
