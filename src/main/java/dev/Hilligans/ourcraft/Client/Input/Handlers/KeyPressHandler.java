package dev.Hilligans.ourcraft.Client.Input.Handlers;

import dev.Hilligans.ourcraft.Client.Input.InputHandler;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IInputProvider;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class KeyPressHandler implements IInputProvider {

    public long window;
    public InputHandler inputHandler;
    public int offset;

    public KeyPressHandler(long window, InputHandler inputHandler) {
        this.window = window;
        this.inputHandler = inputHandler;
        GLFW.glfwSetKeyCallback(window, new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {

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
}
