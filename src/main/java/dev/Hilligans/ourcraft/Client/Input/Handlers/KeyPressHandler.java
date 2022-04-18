package dev.Hilligans.ourcraft.Client.Input.Handlers;

import dev.Hilligans.ourcraft.Client.Input.InputHandler;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IInputProvider;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class KeyPressHandler implements IInputProvider {

    public RenderWindow window;
    public InputHandler inputHandler;
    public int offset;

    @Override
    public void setWindow(RenderWindow window, InputHandler inputHandler) {
        this.window = window;
        this.inputHandler = inputHandler;
        KeyPressHandler handler = this;
        GLFW.glfwSetKeyCallback(window.getWindowID(), new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                inputHandler.handleInput(key,action,window,action,scancode,mods,handler);
            }
        });
    }

    @Override
    public int getSize() {
        return GLFW.GLFW_KEY_LAST;
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
        return GLFW.glfwGetKeyName(button, extra);
    }
}
