package dev.Hilligans.ourcraft.Client.Input.Handlers;

import dev.Hilligans.ourcraft.Client.Input.InputHandler;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IInputProvider;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWJoystickCallback;
import org.lwjgl.glfw.GLFWKeyCallback;

public class ControllerHandler implements IInputProvider {

    public long window;
    public InputHandler inputHandler;
    public int offset;

    public ControllerHandler(long window, InputHandler inputHandler) {
        this.window = window;
        this.inputHandler = inputHandler;
        GLFW.glfwSetJoystickCallback(new GLFWJoystickCallback() {
            @Override
            public void invoke(int jid, int event) {

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
