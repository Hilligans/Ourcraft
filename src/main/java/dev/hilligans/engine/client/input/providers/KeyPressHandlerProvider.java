package dev.hilligans.engine.client.input.providers;

import dev.hilligans.engine.client.input.InputHandlerProvider;
import dev.hilligans.engine.client.input.handlers.KeyPressHandler;
import dev.hilligans.engine.client.graphics.api.IInputProvider;

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
