package Hilligans.Client.Rendering.Widgets;

import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Client.Rendering.Texture;
import Hilligans.Client.Rendering.World.StringRenderer;
import Hilligans.ClientMain;
import org.lwjgl.opengl.GL11;

public class TexturedButton extends Button {

    Texture texture;

    public TexturedButton(int x, int y, int width, int height, String name, Texture texture, ButtonAction buttonAction) {
        super(x, y, width, height, name, buttonAction);
        this.texture = texture;
    }

    @Override
    public void render(MatrixStack matrixStack, int xOffset, int yOffset) {
        super.render(matrixStack, xOffset, yOffset);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        Renderer.drawTexture(matrixStack, texture,x,y,width,height);
        StringRenderer.drawString(matrixStack, name,x,y,0.5f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }
}
