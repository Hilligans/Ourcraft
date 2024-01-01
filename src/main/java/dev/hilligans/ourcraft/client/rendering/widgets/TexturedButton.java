package dev.hilligans.ourcraft.client.rendering.widgets;

import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.Texture;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import org.lwjgl.opengl.GL11;

public class TexturedButton extends Button {

    Texture texture;

    public TexturedButton(int x, int y, int width, int height, String name, Texture texture, ButtonAction buttonAction) {
        super(x, y, width, height, name, buttonAction);
        this.texture = texture;
    }

    @Override
    public void render(RenderWindow window, GraphicsContext graphicsContext, MatrixStack matrixStack, int xOffset, int yOffset) {
        super.render(window, graphicsContext, matrixStack, xOffset, yOffset);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        texture.drawTexture(window, graphicsContext, matrixStack,x,y,width,height);
        window.getStringRenderer().drawStringTranslated(window, graphicsContext, matrixStack, name,x,y,0.5f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }
}
