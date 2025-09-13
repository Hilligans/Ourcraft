package dev.hilligans.ourcraft.client.rendering.widgets;

import dev.hilligans.engine.client.graphics.MatrixStack;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;

import java.util.ArrayList;

public class NestedWidget extends Widget {

    public ArrayList<Widget> widgets = new ArrayList<>();

    public NestedWidget(int x, int y, int width, int height) {
        super(x, y, width, height);
    }



    public NestedWidget addWidget(Widget widget) {
        widgets.add(widget);
        return this;
    }

    @Override
    public void render(RenderWindow window, GraphicsContext graphicsContext, MatrixStack matrixStack, int xOffset, int yOffset) {
        for(Widget widget : widgets) {
            widget.render(window, graphicsContext, matrixStack, getX() + xOffset, getY() + yOffset);
        }
    }
}
