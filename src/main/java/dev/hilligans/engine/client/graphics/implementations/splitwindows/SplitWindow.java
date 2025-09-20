package dev.hilligans.engine.client.graphics.implementations.splitwindows;

import dev.hilligans.engine.application.IClientApplication;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;

import java.util.ArrayList;

public class SplitWindow extends RenderWindow {

    public int windowCountX;
    public int windowCountY;

    public ArrayList<SubWindow> windows = new ArrayList<>();

    public RenderWindow baseWindow;


    public SplitWindow(IGraphicsEngine<?,?,?> graphicsEngine, RenderWindow baseWindow) {
        super(graphicsEngine);
        this.baseWindow = baseWindow;
    }

    public void addWindow(SubWindow window) {
        windows.add(window);
    }

    @Override
    public long getWindowID() {
        return baseWindow.getWindowID();
    }

    @Override
    public void close() {
        baseWindow.close();
    }

    @Override
    public boolean shouldClose() {
        return baseWindow.shouldClose();
    }

    @Override
    public void swapBuffers(GraphicsContext graphicsContext) {
        super.swapBuffers(graphicsContext);
        baseWindow.swapBuffers(graphicsContext);
    }

    @Override
    public IClientApplication getClient() {
        return baseWindow.getClient();
    }

    @Override
    public String getClipboardString() {
        return baseWindow.getClipboardString();
    }

    @Override
    public void setMousePosition(int x, int y) {
        baseWindow.setMousePosition(x, y);
    }

    @Override
    public int getWindowWidth() {
        return baseWindow.getWindowWidth();
    }

    @Override
    public int getWindowHeight() {
        return baseWindow.getWindowHeight();
    }

    @Override
    public boolean isWindowFocused() {
        return baseWindow.isWindowFocused();
    }

    @Override
    public String getWindowingName() {
        return baseWindow.getWindowingName();
    }
}
