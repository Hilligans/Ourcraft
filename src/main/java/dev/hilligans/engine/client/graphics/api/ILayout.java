package dev.hilligans.engine.client.graphics.api;

import dev.hilligans.engine.application.IClientApplication;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.engine.client.graphics.RenderWindow;

public interface ILayout {

    void drawLayout(RenderWindow renderWindow, GraphicsContext graphicsContext, IGraphicsEngine<?,?,?> engine, MatrixStack matrixStack, IClientApplication client);

    void setField(String fieldName, String data);

    void setField(String fieldName, int data);

}
