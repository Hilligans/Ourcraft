package dev.hilligans.ourcraft.client.rendering.widgets;

import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.Textures;

public class DataWidget extends Widget {

    String val;
    int type;

    public DataWidget(int x, int y, int width, int height, int type, String val) {
        super(x, y, width, height);
        this.type = type;
        this.val = val;
    }

    public DataWidget(int width, int height, int type, String val, String name) {
        super(0, 0, width, height);
        this.type = type;
        if(!name.equals("")) {
           this.val = name + ": " + val;
        } else {
            this.val = val;
        }
    }

    @Override
    public void render(RenderWindow window, MatrixStack matrixStack, int xOffset, int yOffset) {
        super.render(window, matrixStack, xOffset, yOffset);
        if (this.getY() > 0 && this.getY() < window.getWindowHeight() && this.isActive()) {
            if (shouldRender) {
                switch (type) {
                    case 0:
                        Textures.BYTE_ICON.drawTexture(window, matrixStack, getX() + xOffset + FolderWidget.size, getY() + yOffset, FolderWidget.size, FolderWidget.size);
                        break;
                    case 1:
                        Textures.SHORT_ICON.drawTexture(window, matrixStack, getX() + xOffset + FolderWidget.size, getY() + yOffset, FolderWidget.size, FolderWidget.size);
                        break;
                    case 2:
                        Textures.INTEGER_ICON.drawTexture(window, matrixStack, getX() + xOffset + FolderWidget.size, getY() + yOffset, FolderWidget.size, FolderWidget.size);
                        break;
                    case 3:
                        Textures.FLOAT_ICON.drawTexture(window, matrixStack, getX() + xOffset + FolderWidget.size, getY() + yOffset, FolderWidget.size, FolderWidget.size);
                        break;
                    case 4:
                        Textures.LONG_ICON.drawTexture(window, matrixStack, getX() + xOffset + FolderWidget.size, getY() + yOffset, FolderWidget.size, FolderWidget.size);
                        break;
                    case 5:
                        Textures.DOUBLE_ICON.drawTexture(window, matrixStack, getX() + xOffset + FolderWidget.size, getY() + yOffset, FolderWidget.size, FolderWidget.size);
                        break;
                }
                window.getStringRenderer().drawStringInternal(window, matrixStack, val, getX() + xOffset + FolderWidget.size * 2, getY() + yOffset, 0.5f);
            }
        }
    }
}
