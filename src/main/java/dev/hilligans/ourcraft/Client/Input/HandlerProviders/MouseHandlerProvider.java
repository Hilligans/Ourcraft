package dev.hilligans.ourcraft.Client.Input.HandlerProviders;

import dev.hilligans.ourcraft.Client.Input.Handlers.MouseHandler;
import dev.hilligans.ourcraft.Client.Input.InputHandlerProvider;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.IInputProvider;

public class MouseHandlerProvider extends InputHandlerProvider {

    public MouseHandlerProvider() {
        super("mouse_button");
    }

    @Override
    public IInputProvider getProvider(String engineName, String windowingName) {
        if(windowingName.equals("glfw")) {
            return new MouseHandler();
        }
        return null;
    }
}
