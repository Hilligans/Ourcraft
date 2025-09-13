package dev.hilligans.engine.client.graphics.api;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.engine.client.graphics.MatrixStack;
import dev.hilligans.engine.client.graphics.RenderWindow;

public interface ILayout {

    void drawLayout(RenderWindow renderWindow, GraphicsContext graphicsContext, IGraphicsEngine<?,?,?> engine, MatrixStack matrixStack, Client client);

    void setField(String fieldName, String data);

    void setField(String fieldName, int data);

}
