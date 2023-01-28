package dev.hilligans.ourcraft.Client.Rendering.Widgets;

import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;

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
    public void render(RenderWindow window, MatrixStack matrixStack, int xOffset, int yOffset) {
        for(Widget widget : widgets) {
            widget.render(window, matrixStack,getX() + xOffset, getY() + yOffset);
        }
    }
}
