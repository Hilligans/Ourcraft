package dev.Hilligans.Ourcraft.Client.Rendering;

import dev.Hilligans.Ourcraft.Client.MatrixStack;

public abstract class Model {

    abstract void render(MatrixStack matrixStack);
    abstract void render2D(MatrixStack matrixStack, int x, int y, float size);

}
