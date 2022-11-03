package dev.Hilligans.ourcraft.Client.Rendering.Graphics.OpenGL;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.Input.Key.KeyHandler;
import dev.Hilligans.ourcraft.Client.Input.Key.MouseHandler;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.FrameTracker;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Implementations.FreeCamera;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Client.ScreenShot;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class OpenGLWindow extends RenderWindow {

    public long window;
    public Client client;
    public boolean shouldClose = false;
    public boolean mouseLocked = false;
    public boolean windowFocused = true;
    public float width;
    public float height;

    public OpenGLWindow(Client client, OpenGLEngine engine, String name, int width, int height) {
        super(engine);
        this.camera = new FreeCamera(this);
        window = glfwCreateWindow(width,height,name,NULL,NULL);
        this.width = width;
        this.height = height;
        if(window == NULL) {
            //glfwTerminate();
            throw new RuntimeException("Failed to create window");
        }
        glfwMakeContextCurrent(window);
        this.client = client;
        registerCallbacks();
    }

    public void setup() {
       super.setup();
       glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
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
        glfwSwapInterval(0);
        glfwSwapBuffers(window);
        client.rendering = false;
        client.soundEngine.tick();
        if(client.screenShot) {
            client.screenShot = false;
            ScreenShot.takeScreenShot(this);
        }
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

    public void registerCallbacks() {
        glfwSetCursorPosCallback(window, (window, xpos, ypos) -> {
            mouseX = xpos;
            mouseY = ypos;
            if(mouseLocked) {
                double halfWindowX = (double) getWindowWidth() / 2;
                double halfWindowY = (double) getWindowHeight() / 2;

                double deltaX = xpos - halfWindowX;
                double deltaY = ypos - halfWindowY;

                if(camera != null) {
                 //   camera.addRotation((float) (deltaY / camera.getSensitivity()), (float) (deltaX / camera.getSensitivity()));
                }
                glfwSetCursorPos(window, halfWindowX, halfWindowY);
            }
        });
        glfwSetWindowSizeCallback(window, (window, w, h) -> {
            width = w;
            height = h;
            client.windowX = w;
            client.windowY = h;
            GL30.glViewport(0,0,w,h);
        });

        glfwSetWindowFocusCallback(window, (window, focused) -> windowFocused = focused);
        MouseHandler mouseHandler = new MouseHandler(client);
        glfwSetMouseButtonCallback(window, mouseHandler::invoke);
        //inputHandler.add((IInputProvider) mouseHandler);
    }
}
