package dev.hilligans.engine.client.graphics.layout;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.engine.client.graphics.MatrixStack;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.client.graphics.api.ILayout;
import dev.hilligans.ourcraft.client.rendering.widgets.Widget;

public class SimpleLayout implements ILayout {

    Widget widget;

    public SimpleLayout(Widget widget) {
        this.widget = widget;
    }

    @Override
    public void drawLayout(RenderWindow renderWindow, GraphicsContext graphicsContext, IGraphicsEngine<?, ?, ?> engine, MatrixStack matrixStack, Client client) {

    }

    @Override
    public void setField(String fieldName, String data) {

    }

    @Override
    public void setField(String fieldName, int data) {

    }
}
