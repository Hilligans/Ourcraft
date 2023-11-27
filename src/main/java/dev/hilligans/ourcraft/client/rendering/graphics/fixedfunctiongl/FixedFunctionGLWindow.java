package dev.hilligans.ourcraft.client.rendering.graphics.fixedfunctiongl;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.rendering.graphics.implementations.FreeCamera;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class FixedFunctionGLWindow extends RenderWindow {

    public long window;
    public Client client;
    public boolean shouldClose = false;
    public boolean mouseLocked = false;
    public boolean windowFocused = true;
    public int width;
    public int height;

    public FixedFunctionGLWindow(Client client, FixedFunctionGLEngine engine) {
        super(engine);
        this.camera = new FreeCamera(this);
        window = glfwCreateWindow(1920,1080,"Ourcraft",NULL,NULL);
        if(window == NULL) {

            glfwTerminate();
            throw new RuntimeException("Failed to create window");
        }
        glfwMakeContextCurrent(window);
        this.client = client;
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
    public boolean shouldClose() {
        return glfwWindowShouldClose(window) || shouldClose;
    }

    @Override
    public void swapBuffers() {
        glfwSwapBuffers(window);
        client.rendering = false;
        glfwPollEvents();
        tick();
    }

    @Override
    public Client getClient() {
        return client;
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
