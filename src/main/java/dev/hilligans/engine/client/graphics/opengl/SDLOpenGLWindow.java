package dev.hilligans.engine.client.graphics.opengl;

import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import org.lwjgl.sdl.*;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.sdl.SDLEvents.SDL_EVENT_QUIT;
import static org.lwjgl.sdl.SDLEvents.SDL_PollEvent;
import static org.lwjgl.sdl.SDLVideo.*;

public class SDLOpenGLWindow extends OpenGLWindow {

    public long gl_context;

    public SDLOpenGLWindow(OpenGLEngine engine, String name, int width, int height, long otherID) {
        super(engine, name, width, height, otherID);

        this.window = SDL_CreateWindow("My SDL Window", width, height, SDL_WINDOW_OPENGL);

        if(this.window == 0) {
            throw new IllegalStateException("Failed to create SDL window:" + SDLError.SDL_GetError());
        }

        gl_context = SDL_GL_CreateContext(window);

        if(gl_context == 0) {
            throw new IllegalStateException("Failed to create gl context:" + SDLError.SDL_GetError());
        }

        SDL_GL_MakeCurrent(this.window, gl_context);
    }

    @Override
    public void swapBuffers(GraphicsContext graphicsContext) {
        super.swapBuffers(graphicsContext);
        try(var $ = graphicsContext.getSection().startSection("glfw_swap_buffers")) {
            SDL_GL_SwapWindow(window);
        }
    }

    @Override
    public boolean shouldClose() {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            SDL_Event event = SDL_Event.malloc(memoryStack);
            SDL_PollEvent(event);

            return event.type() == SDL_EVENT_QUIT;
        }
    }

    @Override
    public void close() {
        super.close();
        SDL_GL_DestroyContext(gl_context);
        SDL_DestroyWindow(window);
    }
}
