package dev.Hilligans.ourcraft.Client.Rendering.Widgets;

import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Textures;
import dev.Hilligans.ourcraft.Client.Rendering.World.StringRenderer;
import dev.Hilligans.ourcraft.ClientMain;

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
    public void render(MatrixStack matrixStack, int xOffset, int yOffset) {
        super.render(matrixStack, xOffset, yOffset);
        if (this.getY() > 0 && this.getY() < ClientMain.getWindowY() && this.isActive()) {
            if (shouldRender) {
                switch (type) {
                    case 0:
                        Textures.BYTE_ICON.drawTexture(matrixStack, getX() + xOffset + FolderWidget.size, getY() + yOffset, FolderWidget.size, FolderWidget.size);
                        break;
                    case 1:
                        Textures.SHORT_ICON.drawTexture(matrixStack, getX() + xOffset + FolderWidget.size, getY() + yOffset, FolderWidget.size, FolderWidget.size);
                        break;
                    case 2:
                        Textures.INTEGER_ICON.drawTexture(matrixStack, getX() + xOffset + FolderWidget.size, getY() + yOffset, FolderWidget.size, FolderWidget.size);
                        break;
                    case 3:
                        Textures.FLOAT_ICON.drawTexture(matrixStack, getX() + xOffset + FolderWidget.size, getY() + yOffset, FolderWidget.size, FolderWidget.size);
                        break;
                    case 4:
                        Textures.LONG_ICON.drawTexture(matrixStack, getX() + xOffset + FolderWidget.size, getY() + yOffset, FolderWidget.size, FolderWidget.size);
                        break;
                    case 5:
                        Textures.DOUBLE_ICON.drawTexture(matrixStack, getX() + xOffset + FolderWidget.size, getY() + yOffset, FolderWidget.size, FolderWidget.size);
                        break;
                }
                StringRenderer.drawString(matrixStack, val, getX() + xOffset + FolderWidget.size * 2, getY() + yOffset, 0.5f);
            }
        }
    }
}
