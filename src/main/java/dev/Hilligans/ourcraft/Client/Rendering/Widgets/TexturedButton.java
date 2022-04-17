package dev.Hilligans.ourcraft.Client.Rendering.Widgets;

import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Client.Rendering.Texture;
import dev.Hilligans.ourcraft.Client.Rendering.World.StringRenderer;
import org.lwjgl.opengl.GL11;

public class TexturedButton extends Button {

    Texture texture;

    public TexturedButton(int x, int y, int width, int height, String name, Texture texture, ButtonAction buttonAction) {
        super(x, y, width, height, name, buttonAction);
        this.texture = texture;
    }

    @Override
    public void render(RenderWindow window, MatrixStack matrixStack, int xOffset, int yOffset) {
        super.render(window, matrixStack, xOffset, yOffset);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        texture.drawTexture(window, matrixStack,x,y,width,height);
        window.getStringRenderer().drawStringTranslated(window, matrixStack, name,x,y,0.5f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }
}
