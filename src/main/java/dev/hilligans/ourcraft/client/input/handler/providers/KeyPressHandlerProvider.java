package dev.hilligans.ourcraft.client.input.handler.providers;

import dev.hilligans.ourcraft.client.input.InputHandlerProvider;
import dev.hilligans.ourcraft.client.input.handlers.KeyPressHandler;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IInputProvider;

public class KeyPressHandlerProvider extends InputHandlerProvider {
    public KeyPressHandlerProvider() {
        super("key_press");
    }

    @Override
    public IInputProvider getProvider(String engineName, String windowingName) {
        if(windowingName.equals("glfw")) {
            return new KeyPressHandler();
        }

        return null;
    }
}
