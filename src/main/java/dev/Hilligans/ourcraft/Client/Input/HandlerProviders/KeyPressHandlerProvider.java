package dev.Hilligans.ourcraft.Client.Input.HandlerProviders;

import dev.Hilligans.ourcraft.Client.Input.Handlers.KeyPressHandler;
import dev.Hilligans.ourcraft.Client.Input.InputHandlerProvider;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IInputProvider;

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
