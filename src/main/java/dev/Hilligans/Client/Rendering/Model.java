package dev.Hilligans.Client.Rendering;

import dev.Hilligans.Client.MatrixStack;

public abstract class Model {

    abstract void render(MatrixStack matrixStack);
    abstract void render2D(MatrixStack matrixStack, int x, int y, float size);

}
