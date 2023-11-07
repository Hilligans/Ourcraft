package dev.hilligans.ourcraft.client.rendering.graphics.implementations;

import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import org.jetbrains.annotations.NotNull;

public class FreeCamera extends WorldCamera {

    RenderWindow window;

    public FreeCamera(RenderWindow renderWindow) {
        this.window = renderWindow;
    }

    @Override
    public @NotNull RenderWindow getWindow() {
        return window;
    }

}
