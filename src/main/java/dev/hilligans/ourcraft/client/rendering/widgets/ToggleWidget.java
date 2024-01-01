package dev.hilligans.ourcraft.client.rendering.widgets;

import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.lang.Languages;
import dev.hilligans.ourcraft.client.rendering.Textures;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import org.lwjgl.opengl.GL11;

public class ToggleWidget extends Widget {

    public boolean value;
    public boolean enabled = true;
    public ToggleAction toggleAction;

    public ToggleWidget(int x, int y, int width, int height, String name, boolean defaultValue, ToggleAction toggleAction) {
        super(x, y, width, height);
        this.toggleAction = toggleAction;
        this.name = name;
        this.value = defaultValue;
    }

    public ToggleWidget(int x, int y, int width, int height, String name, boolean defaultValue) {
        super(x, y, width, height);
        this.toggleAction = value -> value = !value;
        this.name = name;
        this.value = defaultValue;
    }

    public ToggleWidget isEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public void render(RenderWindow window, GraphicsContext graphicsContext, MatrixStack matrixStack, int xOffset, int yOffset) {
        super.render(window, graphicsContext, matrixStack, xOffset, yOffset);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        if(enabled) {
            Textures.BUTTON.drawTexture(window, graphicsContext, matrixStack, x, y, width, height);
        } else {
            Textures.BUTTON_DARK.drawTexture(window, graphicsContext, matrixStack,x,y,width,height);
        }
        window.getStringRenderer().drawCenteredStringInternal(window, graphicsContext, matrixStack, Languages.getTranslated(name) + (value ? ": ON" : ": OFF"),x + width / 2,y,0.5f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    @Override
    public void activate(int x, int y) {
        super.activate(x, y);
        if(enabled) {
            value = !value;
            toggleAction.onPress(value);
        }
    }

}
