package dev.hilligans.engine.client.graphics.opengl;

import dev.hilligans.engine.application.IClientApplication;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.data.Tuple;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL30;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static org.lwjgl.glfw.GLFW.*;

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
    public void render(GraphicsContext graphicsContext, IClientApplication client, MatrixStack worldStack, MatrixStack screenStack) {
        glfwMakeContextCurrent(window);
        if(updatedSize) {
            GL30.glViewport(0, 0, width, height);
            updatedSize = false;
        }
        super.render(graphicsContext, client, worldStack, screenStack);
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
    public String getClipboardString() {
        return glfwGetClipboardString(window);
    }

    @Override
    public void setMousePosition(int x, int y) {
        glfwSetCursorPos(window, x, y);
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

    @Override
    public String getWindowingName() {
        return "glfw";
    }

}
