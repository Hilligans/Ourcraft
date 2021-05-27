package Hilligans.Client.Rendering.Widgets;

import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Client.Rendering.Textures;
import Hilligans.Client.Rendering.World.StringRenderer;

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
            Renderer.drawTexture(matrixStack, Textures.BUTTON, x, y, width, height);
        } else {
            Renderer.drawTexture(matrixStack, Textures.BUTTON_DARK,x,y,width,height);
        }
        StringRenderer.drawString(matrixStack,name,x,y,0.5f);
    }

    @Override
    public void activate(int x, int y) {
        selectorScreen.setActive(this);
        enabled = false;
    }
}
