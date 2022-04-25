package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Implementations.SplitWindows;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;

import java.util.ArrayList;

public class FloatingWindow extends RenderWindow {

    public ArrayList<RenderWindow> windows = new ArrayList<>();


    public FloatingWindow(IGraphicsEngine<?,?> graphicsEngine) {
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
