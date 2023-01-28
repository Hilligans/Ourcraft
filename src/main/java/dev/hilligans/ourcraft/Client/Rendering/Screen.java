package dev.hilligans.ourcraft.Client.Rendering;

import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;

public interface Screen {

    void setWindow(RenderWindow renderWindow);

    default void render(RenderWindow window, MatrixStack matrixStack) {}

    default void close(boolean replaced) {}

    default void mouseClick(int x, int y, int mouseButton) {}

    default void mouseScroll(int x, int y, float amount) {}

    default void mouseDrag(int x, int y) {}

    default void resize(int x, int y) {}

    default boolean renderWorld() {return true;}
}
