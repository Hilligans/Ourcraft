package dev.Hilligans.ourcraft.Client.Rendering;

import dev.Hilligans.ourcraft.Client.MatrixStack;

public abstract class Model {

    abstract void render(MatrixStack matrixStack);
    abstract void render2D(MatrixStack matrixStack, int x, int y, float size);

}
