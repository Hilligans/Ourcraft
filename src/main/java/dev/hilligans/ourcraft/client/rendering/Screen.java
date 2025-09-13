package dev.hilligans.ourcraft.client.rendering;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.engine.client.graphics.MatrixStack;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;

public interface Screen {

    void setWindow(RenderWindow renderWindow);

    RenderWindow getWindow();

    default Client getClient() {
        return getWindow().getClient();
    }

    default void render(RenderWindow window, MatrixStack matrixStack, GraphicsContext graphicsContext) {}

    default void close(boolean replaced) {}

    default void mouseClick(int x, int y, int mouseButton) {}

    default void mouseScroll(int x, int y, float amount) {}

    default void mouseDrag(int x, int y) {}

    default void resize(int x, int y) {}

}
