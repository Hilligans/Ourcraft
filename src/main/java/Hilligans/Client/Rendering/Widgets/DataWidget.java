package Hilligans.Client.Rendering.Widgets;

import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Client.Rendering.Texture;
import Hilligans.Client.Rendering.Textures;
import Hilligans.Client.Rendering.World.StringRenderer;
import Hilligans.ClientMain;

public class DataWidget extends Widget {

    String val;
    int type;
    String name;

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
                        Renderer.drawTexture(matrixStack, Textures.BYTE_ICON, getX() + xOffset + FolderWidget.size, getY() + yOffset, FolderWidget.size, FolderWidget.size);
                        break;
                    case 1:
                        Renderer.drawTexture(matrixStack, Textures.SHORT_ICON, getX() + xOffset + FolderWidget.size, getY() + yOffset, FolderWidget.size, FolderWidget.size);
                        break;
                    case 2:
                        Renderer.drawTexture(matrixStack, Textures.INTEGER_ICON, getX() + xOffset + FolderWidget.size, getY() + yOffset, FolderWidget.size, FolderWidget.size);
                        break;
                    case 3:
                        Renderer.drawTexture(matrixStack, Textures.FLOAT_ICON, getX() + xOffset + FolderWidget.size, getY() + yOffset, FolderWidget.size, FolderWidget.size);
                        break;
                    case 4:
                        Renderer.drawTexture(matrixStack, Textures.LONG_ICON, getX() + xOffset + FolderWidget.size, getY() + yOffset, FolderWidget.size, FolderWidget.size);
                        break;
                    case 5:
                        Renderer.drawTexture(matrixStack, Textures.DOUBLE_ICON, getX() + xOffset + FolderWidget.size, getY() + yOffset, FolderWidget.size, FolderWidget.size);
                        break;
                }
                StringRenderer.drawString(matrixStack, val, getX() + xOffset + FolderWidget.size * 2, getY() + yOffset, 0.5f);
            }
        }
    }
}
