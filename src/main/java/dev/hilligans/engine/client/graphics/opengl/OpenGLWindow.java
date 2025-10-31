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
import static org.lwjgl.system.MemoryUtil.NULL;

public class OpenGLWindow extends RenderWindow {

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

        window = glfwCreateWindow(width, height, name, NULL, otherID);
        System.out.println(window);
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
        if(!mainThread) {
            while(true) {
                long owner = ownerWindowID.get();
                if (owner == 0) {
                    if(ownerWindowID.compareAndExchange(0, window) == 0) {
                        mainThread = true;
                        break;
                    }
                } else {
                    handlingQueue.add(new Tuple<>(this, graphicsContext));
                    try {
                        this.swapSemaphore.acquire();
                    } catch (InterruptedException e) {
                        continue;
                    }
                    return;
                }
            }
        }

        glfwMakeContextCurrent(window);
        swap(graphicsContext);
        Tuple<OpenGLWindow, GraphicsContext> otherWindow;
        while((otherWindow = handlingQueue.poll()) != null) {
            GraphicsContext context = otherWindow.typeB;
            OpenGLWindow window = otherWindow.typeA;

            glfwMakeContextCurrent(window.window);
            window.swapBuffers(context);

            window.swapSemaphore.release();
        }
    }

    public void swap(GraphicsContext graphicsContext) {
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
