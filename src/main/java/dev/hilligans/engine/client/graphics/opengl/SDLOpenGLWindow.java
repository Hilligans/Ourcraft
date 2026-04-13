package dev.hilligans.engine.client.graphics.opengl;

import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import org.lwjgl.sdl.*;

import static org.lwjgl.sdl.SDLClipboard.SDL_GetClipboardText;
import static org.lwjgl.sdl.SDLEvents.*;
import static org.lwjgl.sdl.SDLMouse.SDL_WarpMouseInWindow;
import static org.lwjgl.sdl.SDLTimer.SDL_Delay;
import static org.lwjgl.sdl.SDLVideo.*;

public class SDLOpenGLWindow extends OpenGLWindow {

    public long gl_context;
    public boolean open = true;

    public SDLOpenGLWindow(OpenGLEngine engine, String name, int width, int height, long otherID) {
        super(engine, name, width, height, otherID);

        SDL_AddEventWatch(new SDL_EventFilter() {
            @Override
            public boolean invoke(long userdata, long event) {
                SDL_Event sdlEvent = SDL_Event.createSafe(event);

                if(sdlEvent == null) {
                    return false;
                }

                if(sdlEvent.type() == SDL_EVENT_QUIT) {
                    open = false;
                } else if(sdlEvent.type() == SDL_EVENT_KEY_DOWN) {
                    System.out.println(sdlEvent.key().key());
                } else {
                    return false;
                }

                return true;
            }
        }, 0);

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
        SDL_PumpEvents();
        try(var $ = graphicsContext.getSection().startSection("sdl_swap_buffers")) {
            SDL_GL_SwapWindow(window);
        }
    }

    @Override
    public boolean shouldClose() {
        SDL_Delay(10);
        return !open;
    }

    @Override
    public String getClipboardString() {
        return SDL_GetClipboardText();
    }

    @Override
    public void setMousePosition(int x, int y) {
        SDL_WarpMouseInWindow(window, x, y);
    }

    @Override
    public void close() {
        super.close();
        SDL_GL_DestroyContext(gl_context);
        SDL_DestroyWindow(window);
    }

    @Override
    public String getWindowingName() {
        return "sdl";
    }
}
