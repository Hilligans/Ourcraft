package dev.hilligans.ourcraft.client.rendering.widgets;

import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;

public class InvisibleFolder extends FolderWidget {
    public InvisibleFolder(int x, int y, int width, int height, String name) {
        super(x, y, width, height, name);
        isOpen = true;
    }

    @Override
    public void render(RenderWindow window, MatrixStack matrixStack, int xOffset, int yOffset) {
        for (Widget widget : widgets) {
            widget.render(window, matrixStack, xOffset, yOffset);
        }
    }

    @Override
    public void activate(int x, int y) {
        for (Widget widget : widgets) {
            if (widget.isInBounds(x + this.getX(), y + this.getY())) {
                widget.activate(x - widget.getX() + this.getX(), y - widget.getY() + this.getY());
                return;
            }
        }
    }
}
