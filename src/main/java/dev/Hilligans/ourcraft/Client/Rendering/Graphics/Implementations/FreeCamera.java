package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Implementations;

import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4d;

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
