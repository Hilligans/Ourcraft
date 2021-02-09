package Hilligans.Client.Rendering;

import Hilligans.Client.MatrixStack;

public interface Screen {




    default void render(MatrixStack matrixStack) {}

    default void close() {}

    default void mouseClick(int x, int y, int mouseButton) {}

}
