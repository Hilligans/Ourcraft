package Hilligans.Client.Rendering.Widgets;

import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Client.Rendering.Textures;
import Hilligans.Client.Rendering.World.StringRenderer;
import Hilligans.ClientMain;

public class InvisibleFolder extends FolderWidget {
    public InvisibleFolder(int x, int y, int width, int height, String name) {
        super(x, y, width, height, name);
        isOpen = true;
    }

    @Override
    public void render(MatrixStack matrixStack, int xOffset, int yOffset) {
        for (Widget widget : widgets) {
            widget.render(matrixStack, xOffset, yOffset);
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
