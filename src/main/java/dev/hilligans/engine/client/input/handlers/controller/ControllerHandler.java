package dev.hilligans.engine.client.input.handlers.controller;

import dev.hilligans.engine.client.input.InputHandler;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.IInputProvider;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;
import org.lwjgl.glfw.GLFWJoystickCallback;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public class ControllerHandler implements IInputProvider {

    public RenderWindow window;
    public ArrayList<InputHandler> inputHandlers = new ArrayList<>();
    public Long2ObjectOpenHashMap<InputHandler> windowMap = new Long2ObjectOpenHashMap<>();

    public int offset;

    public long[] windowBind = new long[GLFW.GLFW_JOYSTICK_LAST];
    public boolean[] controllers = new boolean[GLFW.GLFW_JOYSTICK_LAST];
    public boolean[] isGamePad = new boolean[GLFW.GLFW_JOYSTICK_LAST];

    public byte[][] buttonPresses = new byte[GLFW.GLFW_JOYSTICK_LAST][15];
    public float[][] axes = new float[GLFW.GLFW_JOYSTICK_LAST][6];

    public ControllerSettings[] settings = new ControllerSettings[GLFW.GLFW_JOYSTICK_LAST];

    public int largest = -1;

    public boolean initialized = false;

    @Override
    public void setWindow(RenderWindow window, InputHandler inputHandler) {
        this.window = window;
        inputHandlers.add(inputHandler);
        if(!initialized) {
            GLFWJoystickCallback callback = new GLFWJoystickCallback() {
                @Override
                public void invoke(int jid, int event) {
                    if (event == GLFW.GLFW_CONNECTED) {
                        controllers[jid] = true;
                        windowBind[jid] = -1;
                        isGamePad[jid] = GLFW.glfwJoystickIsGamepad(jid);
                        if(jid > largest) {
                            largest = jid;
                        }
                        settings[jid] = getControllerSettings(GLFW.glfwGetJoystickUserPointer(jid), GLFW.glfwGetJoystickGUID(jid));
                    } else if (event == GLFW.GLFW_DISCONNECTED) {
                        controllers[jid] = false;
                        windowBind[jid] = -2;
                        isGamePad[jid] = false;
                    }
                }
            };

            GLFW.glfwSetJoystickCallback(callback);
            window.addResourceCleanup(callback::close);
        }

    }

    //15 buttons and 6 axes
    @Override
    public int getSize() {
        return GLFW.GLFW_JOYSTICK_LAST * (15 + 6);
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
        return true;
    }

    @Override
    public void tick() {
        GLFWGamepadState state = GLFWGamepadState.calloc();
        for (int id = 0; id < largest + 1; id++) {
            InputHandler handler = null;
            if (windowBind[id] != -1) {
                handler = windowMap.get(windowBind[id]);
            }
            if (controllers[id]) {
                if (isGamePad[id] && GLFW.glfwGetGamepadState(id, state)) {
                    ByteBuffer buttons = state.buttons();
                    FloatBuffer axes = state.axes();
                    process(id,handler,buttons,axes);
                } else {
                    ByteBuffer buttons = GLFW.glfwGetJoystickButtons(id);
                    FloatBuffer axes = GLFW.glfwGetJoystickAxes(id);
                    process(id,handler,buttons,axes);
                }
            }
        }
        state.free();
    }

    private void process(int id, InputHandler handler, ByteBuffer buttons, FloatBuffer axes) {
        for (int x = 0; x < Math.min(15,buttons.limit()); x++) {
            if (buttonPresses[id][x] != buttons.get(x)) {
                buttonPresses[id][x] = buttons.get(x);
                handleInput(handler, x, 1, windowBind[id], id, buttons.get(x));
            } else if (buttonPresses[id][x] == GLFW.GLFW_TRUE) {

            }
        }
        for (int x = 0; x < Math.min(6,axes.limit()); x++) {
            if (this.axes[id][x] != axes.get(x)) {
                this.axes[id][x] = axes.get(x);
                handleInput(handler, x + 15, 1, windowBind[id], id, axes.get(x));
            } else if (axes.get(x) != 0) {

            }
        }
    }

    @Override
    public String getButtonName(int button, int id) {
        if(GLFW.glfwJoystickIsGamepad(id)) {
            return getName(button);
        }
        return "Unknown_Button_" + (button);
    }

    private String getName(int id) {
        return switch (id) {
            case 0 -> "A";
            case 1 -> "B";
            case 2 -> "X";
            case 3 -> "Y";
            case 4 -> "Left_Bumper";
            case 5 -> "Right_Bumper";
            case 6 -> "Back";
            case 7 -> "Start";
            case 8 -> "Guide";
            case 9 -> "Left_Thumb_Stick";
            case 10 -> "Right_Thumb_Stick";
            case 11 -> "DPad_Up";
            case 12 -> "DPad_Right";
            case 13 -> "DPad_Down";
            case 14 -> "DPad_Left";
            case 15 -> "Left_Axis_X";
            case 16 -> "Left_Axis_Y";
            case 17 -> "Right_Axis_X";
            case 18 -> "Right_Axis_Y";
            case 19 -> "Left_Trigger";
            case 20 -> "Right_Trigger";
            default -> "Unknown_Controller_Button";
        };
    }

    private void handleInput(InputHandler inputHandler, int button, int value, long window, int extra, float val) {
        if(inputHandler == null) {
            for(InputHandler handler : inputHandlers) {
                handler.handleInput(button,value,window, 0, extra, 0, val, this);
            }
        } else {
            inputHandler.handleInput(button,value,window,0, extra, 0, val, this);
        }
    }

    public ControllerSettings getControllerSettings(long id, String controllerGUID) {



        return new ControllerSettings(id);
    }

    @Override
    public String getResourceName() {
        return "controller_handler";
    }

    @Override
    public String getResourceOwner() {
        return "ourcraft";
    }
}
