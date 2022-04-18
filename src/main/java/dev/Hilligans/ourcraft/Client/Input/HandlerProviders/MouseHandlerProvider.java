package dev.Hilligans.ourcraft.Client.Input.HandlerProviders;

import dev.Hilligans.ourcraft.Client.Input.Handlers.MouseHandler;
import dev.Hilligans.ourcraft.Client.Input.InputHandlerProvider;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IInputProvider;

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
