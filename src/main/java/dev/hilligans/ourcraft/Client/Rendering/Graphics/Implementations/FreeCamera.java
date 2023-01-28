package dev.hilligans.ourcraft.Client.Rendering.Graphics.Implementations;

import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
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
