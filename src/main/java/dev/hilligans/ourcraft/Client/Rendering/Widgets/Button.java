package dev.hilligans.ourcraft.Client.Rendering.Widgets;

import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.hilligans.ourcraft.Client.Rendering.Textures;

public class Button extends Widget {

    public ButtonAction buttonAction;

    public Button(int x, int y, int width, int height, String name, ButtonAction buttonAction) {
        super(x, y, width, height);
        this.name = name;
        this.buttonAction = buttonAction;
        widgetId = 0;
    }

    public Button(int x, int y, int width, int height) {
        this(x,y,width,height,"",null);
    }

    public Button isEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public void render(RenderWindow window, MatrixStack matrixStack, int xOffset, int yOffset) {
        super.render(window, matrixStack, xOffset, yOffset);
        if(enabled) {
            Textures.BUTTON.drawTexture(window, matrixStack, x, y, width, height);
        } else {
            Textures.BUTTON_DARK.drawTexture(window, matrixStack,x,y,width,height);
        }
        window.getStringRenderer().drawCenteredStringInternal(window, matrixStack, name,x + width / 2,y + 5,0.5f);
    }

    @Override
    public void activate(int x, int y) {
        super.activate(x, y);
        if(enabled) {
            buttonAction.onPress();
        }
    }
}
