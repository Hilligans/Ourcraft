package dev.hilligans.ourcraft.Client.Input.HandlerProviders;

import dev.hilligans.ourcraft.Client.Input.Handlers.KeyPressHandler;
import dev.hilligans.ourcraft.Client.Input.InputHandlerProvider;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.IInputProvider;

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
