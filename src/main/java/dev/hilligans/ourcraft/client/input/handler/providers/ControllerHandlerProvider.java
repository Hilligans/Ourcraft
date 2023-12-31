package dev.hilligans.ourcraft.client.input.handler.providers;

import dev.hilligans.ourcraft.client.input.InputHandlerProvider;
import dev.hilligans.ourcraft.client.input.handlers.controller.ControllerHandler;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IInputProvider;

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
