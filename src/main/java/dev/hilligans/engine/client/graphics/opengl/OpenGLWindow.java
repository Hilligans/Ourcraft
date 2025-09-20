package dev.hilligans.engine.client.graphics.opengl;

import dev.hilligans.engine.application.IClientApplication;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
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

    private GLFWWindowSizeCallback sizeCallback;
    private GLFWWindowFocusCallback focusCallback;

    public void registerCallbacks() {
        sizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int w, int h) {
                width = w;
                height = h;
                updatedSize = true;
            }
        };
        glfwSetWindowSizeCallback(window, sizeCallback);


        focusCallback = new GLFWWindowFocusCallback() {
            @Override
            public void invoke(long window, boolean focused) {
                windowFocused = focused;
            }
        };
        glfwSetWindowFocusCallback(window, focusCallback);

        //MouseHandler mouseHandler = new MouseHandler(client);
        //glfwSetMouseButtonCallback(window, mouseHandler::invoke);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        if(sizeCallback != null) {
            sizeCallback.close();
        }
        if(focusCallback != null) {
            focusCallback.close();
        }

        glfwDestroyWindow(window);
    }
}
