package dev.hilligans.engine.client.graphics;

import dev.hilligans.engine.application.IClientApplication;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;

public interface Screen {

    void setWindow(RenderWindow renderWindow);

    RenderWindow getWindow();

    default IClientApplication getClient() {
        return getWindow().getClient();
    }

    default void render(RenderWindow window, MatrixStack matrixStack, GraphicsContext graphicsContext) {}

    default void close(boolean replaced) {}

    default void mouseClick(int x, int y, int mouseButton) {}

    default void mouseScroll(int x, int y, float amount) {}

    default void mouseDrag(int x, int y) {}

    default void resize(int x, int y) {}

}
