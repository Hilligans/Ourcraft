package dev.hilligans.ourcraft.client.input.handler.providers;

import dev.hilligans.ourcraft.client.input.InputHandlerProvider;
import dev.hilligans.ourcraft.client.input.handlers.MouseHandler;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IInputProvider;

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
