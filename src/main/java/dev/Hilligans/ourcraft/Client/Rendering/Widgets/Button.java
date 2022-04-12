package dev.Hilligans.ourcraft.Client.Rendering.Widgets;

import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Textures;
import dev.Hilligans.ourcraft.Client.Rendering.World.StringRenderer;
import dev.Hilligans.ourcraft.Ourcraft;
import org.lwjgl.opengl.GL11;

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
    public void render(MatrixStack matrixStack, int xOffset, int yOffset) {
        super.render(matrixStack, xOffset, yOffset);
        if(enabled) {
            Textures.BUTTON.drawTexture(window, matrixStack, x, y, width, height);
        } else {
            Textures.BUTTON_DARK.drawTexture(window, matrixStack,x,y,width,height);
        }
        StringRenderer.drawCenteredString(matrixStack, name,x + width / 2,y + 5,0.5f);
    }

    @Override
    public void activate(int x, int y) {
        super.activate(x, y);
        if(enabled) {
            buttonAction.onPress();
        }
    }
}
