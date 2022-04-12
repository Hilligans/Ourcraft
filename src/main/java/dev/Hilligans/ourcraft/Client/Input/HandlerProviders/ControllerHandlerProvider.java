package dev.Hilligans.ourcraft.Client.Input.HandlerProviders;

import dev.Hilligans.ourcraft.Client.Input.Handlers.Controller.ControllerHandler;
import dev.Hilligans.ourcraft.Client.Input.InputHandlerProvider;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IInputProvider;

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
