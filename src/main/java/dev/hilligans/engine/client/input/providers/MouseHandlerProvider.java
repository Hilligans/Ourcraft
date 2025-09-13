package dev.hilligans.engine.client.input.providers;

import dev.hilligans.engine.client.input.InputHandlerProvider;
import dev.hilligans.engine.client.input.handlers.MouseHandler;
import dev.hilligans.engine.client.graphics.api.IInputProvider;

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
