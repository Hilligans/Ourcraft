package dev.hilligans.ourcraft.client.rendering.widgets;

import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;

public class SelectorWidget extends Widget {

    SelectorScreen selectorScreen;

    public SelectorWidget(int x, int y, int width, int height, String name, SelectorScreen selectorScreen) {
        super(x, y, width, height);
        this.name = name;
        this.selectorScreen = selectorScreen;
    }

    @Override
    public void render(RenderWindow window, MatrixStack matrixStack, int xOffset, int yOffset) {
        super.render(window, matrixStack, xOffset, yOffset);
        if(enabled) {
           // Textures.BUTTON.drawTexture(matrixStack, x, y, width, height);
        } else {
           // Textures.BUTTON_DARK.drawTexture(matrixStack,x,y,width,height);
        }
        window.getStringRenderer().drawCenteredStringInternal(window, matrixStack,name,x + width / 2,y,0.5f);
    }

    @Override
    public void activate(int x, int y) {
        selectorScreen.setActive(this);
        enabled = false;
    }
}
