package dev.Hilligans.ourcraft.Client.Rendering.Widgets;

import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Textures;
import dev.Hilligans.ourcraft.Client.Rendering.World.StringRenderer;

public class SelectorWidget extends Widget {

    SelectorScreen selectorScreen;

    public SelectorWidget(int x, int y, int width, int height, String name, SelectorScreen selectorScreen) {
        super(x, y, width, height);
        this.name = name;
        this.selectorScreen = selectorScreen;
    }

    @Override
    public void render(MatrixStack matrixStack, int xOffset, int yOffset) {
        super.render(matrixStack, xOffset, yOffset);
        if(enabled) {
           // Textures.BUTTON.drawTexture(matrixStack, x, y, width, height);
        } else {
           // Textures.BUTTON_DARK.drawTexture(matrixStack,x,y,width,height);
        }
        StringRenderer.drawCenteredString(matrixStack,name,x + width / 2,y,0.5f);
    }

    @Override
    public void activate(int x, int y) {
        selectorScreen.setActive(this);
        enabled = false;
    }
}
