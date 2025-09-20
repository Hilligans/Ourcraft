package dev.hilligans.engine.client.input.handlers;

import dev.hilligans.engine.client.input.InputHandler;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.IInputProvider;
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
        GLFWKeyCallback callback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                inputHandler.handleInput(key, action, window, action, scancode, mods, action != 0 ? 1 : 0, handler);
            }
        };
        GLFW.glfwSetKeyCallback(window.getWindowID(), callback);
        window.addResourceCleanup(callback::close);
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

    @Override
    public String getResourceName() {
        return "key_press_handler";
    }

    @Override
    public String getResourceOwner() {
        return "ourcraft";
    }
}
