package dev.hilligans.ourcraft.client.rendering.graphics.opengl;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.input.key.MouseHandler;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.implementations.FreeCamera;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.ScreenShot;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class OpenGLWindow extends RenderWindow {

    public long window;
    public boolean shouldClose = false;
    public boolean mouseLocked = false;
    public boolean windowFocused = true;
    public int width;
    public int height;
    public boolean updatedSize = false;

    public OpenGLWindow(OpenGLEngine engine, String name, int width, int height, long otherID) {
        super(engine);
        this.camera = new FreeCamera(this);
        this.width = width;
        this.height = height;

        window = glfwCreateWindow(width, height, name, NULL, otherID);
        if (window == NULL) {
            glfwTerminate();
            throw new RuntimeException("Failed to create window");
        }

        glfwMakeContextCurrent(window);
        registerCallbacks();
    }

    public void setup() {
       super.setup();
       glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

    @Override
    public void render(GraphicsContext graphicsContext, Client client, MatrixStack worldStack, MatrixStack screenStack) {
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
    public boolean shouldClose() {
        return (window != NULL && glfwWindowShouldClose(window)) || shouldClose;
    }

    @Override
    public void swapBuffers(GraphicsContext graphicsContext) {
        super.swapBuffers(graphicsContext);
        try(var $ = graphicsContext.getSection().startSection("glfw_swap_buffers")) {
            glfwSwapInterval(0);
            glfwSwapBuffers(window);
        }
        client.rendering = false;
        if(getGameInstance().built()) {
            client.soundEngine.tick();
        }
//        if(client.screenShot) {
//            client.screenShot = false;
//            ScreenShot.takeScreenShot(this);
//        }
        glfwPollEvents();
        tick();
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

    public void registerCallbacks() {
        glfwSetWindowSizeCallback(window, (window, w, h) -> {
            width = w;
            height = h;
            updatedSize = true;
        });

        glfwSetWindowFocusCallback(window, (window, focused) -> windowFocused = focused);
        //MouseHandler mouseHandler = new MouseHandler(client);
        //glfwSetMouseButtonCallback(window, mouseHandler::invoke);
    }
}
