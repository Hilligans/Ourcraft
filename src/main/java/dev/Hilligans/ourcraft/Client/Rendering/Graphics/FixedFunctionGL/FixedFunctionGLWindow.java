package dev.Hilligans.ourcraft.Client.Rendering.Graphics.FixedFunctionGL;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Implementations.FreeCamera;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.OpenGL.OpenGLEngine;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Client.ScreenShot;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class FixedFunctionGLWindow extends RenderWindow {

    public long window;
    public Client client;
    public boolean shouldClose = false;
    public boolean mouseLocked = false;
    public boolean windowFocused = true;
    public float width;
    public float height;

    public FixedFunctionGLWindow(Client client, FixedFunctionGLEngine engine) {
        super(engine);
        this.camera = new FreeCamera(this);
        window = glfwCreateWindow(client.windowX,client.windowY,"Ourcraft",NULL,NULL);
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
    public float getWindowWidth() {
        return width;
    }

    @Override
    public float getWindowHeight() {
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
