package dev.hilligans.engine.client.graphics.opengl;

import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.data.Tuple;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

public abstract class OpenGLWindow extends RenderWindow {

    public static ConcurrentLinkedQueue<Tuple<OpenGLWindow, GraphicsContext>> handlingQueue = new ConcurrentLinkedQueue<>();
    public static AtomicLong ownerWindowID = new AtomicLong(0);

    public long window;
    public boolean shouldClose = false;
    public boolean mouseLocked = false;
    public boolean windowFocused = true;
    public int width;
    public int height;
    public boolean updatedSize = false;

    public Semaphore swapSemaphore = new Semaphore(0);
    public boolean mainThread = false;

    public OpenGLWindow(OpenGLEngine engine, String name, int width, int height, long otherID) {
        super(engine);
        this.width = width;
        this.height = height;
    }

    @Override
    public long getWindowID() {
        return window;
    }

    @Override
    public void close() {
        shouldClose = true;
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
        return windowFocused;
    }

}
