package dev.Hilligans.ourcraft.Client.Rendering.Widgets;

import dev.Hilligans.ourcraft.Client.MatrixStack;

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
    public void render(MatrixStack matrixStack, int xOffset, int yOffset) {
        for(Widget widget : widgets) {
            widget.render(matrixStack,getX() + xOffset, getY() + yOffset);
        }
    }
}
