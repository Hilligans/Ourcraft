package dev.Hilligans.Ourcraft.Client.Rendering;

import dev.Hilligans.Ourcraft.Client.MatrixStack;

public interface Screen {


    default void render(MatrixStack matrixStack) {}

    default void close(boolean replaced) {}

    default void mouseClick(int x, int y, int mouseButton) {}

    default void mouseScroll(int x, int y, float amount) {}

    default void mouseDrag(int x, int y) {}

    default void resize(int x, int y) {}

    default boolean renderWorld() {return true;}

}
