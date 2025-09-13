package dev.hilligans.engine.client.input.providers;

import dev.hilligans.engine.client.input.InputHandlerProvider;
import dev.hilligans.engine.client.input.handlers.controller.ControllerHandler;
import dev.hilligans.engine.client.graphics.api.IInputProvider;

public class ControllerHandlerProvider extends InputHandlerProvider {
    public ControllerHandlerProvider() {
        super("controller");
    }

    @Override
    public IInputProvider getProvider(String engineName, String windowingName) {
        if(windowingName.equals("glfw")) {
            return getGLFWInstance();
        }

        return null;
    }


    public static ControllerHandler handler;

    public static ControllerHandler getGLFWInstance() {
        if(handler == null) {
            handler = new ControllerHandler();
        }
        return handler;
    }
}
