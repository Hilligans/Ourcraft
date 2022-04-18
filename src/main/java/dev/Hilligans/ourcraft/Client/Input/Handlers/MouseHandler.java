package dev.Hilligans.ourcraft.Client.Input.Handlers;

import dev.Hilligans.ourcraft.Client.Input.InputHandler;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IInputProvider;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class MouseHandler implements IInputProvider {

    public int offset;
    public InputHandler handler;
    public RenderWindow window;

    @Override
    public void setWindow(RenderWindow window, InputHandler handler) {
        this.window = window;
        this.handler = handler;
        MouseHandler mouse = this;
        GLFW.glfwSetMouseButtonCallback(window.getWindowID(), new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                handler.handleInput(button,action,window,mouse);
            }
        });
    }

    @Override
    public int getSize() {
        return GLFW.GLFW_MOUSE_BUTTON_LAST;
    }

    @Override
    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public boolean requiresTicking() {
        return false;
    }

    @Override
    public void tick() {

    }

    @Override
    public String getButtonName(int button, int extra) {
        if(button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
            return "Mouse Button Middle";
        }
        return "Mouse Button " + button;
    }
}
