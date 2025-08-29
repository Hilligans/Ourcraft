package dev.hilligans.ourcraft.client.rendering.graphics.layout;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.client.rendering.graphics.api.ILayout;
import dev.hilligans.ourcraft.client.rendering.graphics.api.ILayoutEngine;
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
