package dev.hilligans.engine.client.input.handlers;

import dev.hilligans.engine.client.input.InputHandler;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.IInputProvider;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.system.Callback;

public class MouseHandler implements IInputProvider {

    public int offset;
    public InputHandler handler;
    public RenderWindow window;

    @Override
    public void setWindow(RenderWindow window, InputHandler handler) {
        this.window = window;
        this.handler = handler;
        MouseHandler mouse = this;
        GLFWMouseButtonCallback callback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                handler.handleInput(button,action,window,action, mouse);
            }
        };
        GLFW.glfwSetMouseButtonCallback(window.getWindowID(), callback);
        window.addResourceCleanup(callback::close);

        GLFWCursorPosCallback callback1 = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long w, double xpos, double ypos) {
                xpos -= window.getWindowWidth() / 2;
                ypos -= window.getWindowHeight() / 2;
                if(xpos != 0) {
                    handler.handleInput(MOUSE_X, 2, w, 1,0,0, (float) xpos, mouse);
                }
                if(ypos != 0) {
                    handler.handleInput(MOUSE_Y, 2, w, 1,0,0, (float) ypos, mouse);
                }
            }
        };
        GLFW.glfwSetCursorPosCallback(window.getWindowID(), callback1);
        window.addResourceCleanup(callback1::close);
    }

    @Override
    public int getSize() {
        return GLFW.GLFW_MOUSE_BUTTON_LAST + 3;
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
        if(button == MOUSE_X) {
            return "Mouse X";
        }
        if(button == MOUSE_Y) {
            return "Mouse Y";
        }
        return "Mouse Button " + button;
    }

    @Override
    public String getResourceName() {
        return "mouse_handler";
    }

    @Override
    public String getResourceOwner() {
        return "ourcraft";
    }

    public static int MOUSE_BUTTON_1      = 0,
    MOUSE_BUTTON_2      = 1,
    MOUSE_BUTTON_3      = 2,
    MOUSE_BUTTON_4      = 3,
    MOUSE_BUTTON_5      = 4,
    MOUSE_BUTTON_6      = 5,
    MOUSE_BUTTON_7      = 6,
    MOUSE_BUTTON_8      = 7,
    MOUSE_X = 8,
    MOUSE_Y = 9;

}
