package dev.Hilligans.ourcraft.Client.Rendering.Graphics.OpenGL;

import dev.Hilligans.ourcraft.Client.Camera;
import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.IInputProvider;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Client.ScreenShot;

import static org.lwjgl.glfw.GLFW.*;

public class OpenGLWindow extends RenderWindow {

    public long window;
    public Client client;
    public boolean shouldClose = false;

    public OpenGLWindow(long window, Client client) {
        this.window = window;
        this.client = client;
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
        client.soundEngine.tick();
        if(client.screenShot) {
            client.screenShot = false;
            ScreenShot.takeScreenShot();
        }
        glfwPollEvents();
        Camera.updateMouse();
    }

    @Override
    public Client getClient() {
        return client;
    }

    @Override
    public IInputProvider getInputProvider() {
        return null;
    }


}
