package Hilligans.Client.Rendering.Widgets;

import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Client.Rendering.Texture;
import Hilligans.Client.Rendering.Textures;
import Hilligans.Client.Rendering.World.StringRenderer;
import Hilligans.ClientMain;
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
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        if(enabled) {
            Textures.BUTTON.drawTexture(matrixStack, x, y, width, height);
        } else {
            Textures.BUTTON_DARK.drawTexture(matrixStack,x,y,width,height);
        }
        StringRenderer.drawCenteredString(matrixStack, name,x + width / 2,y + 5,0.5f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    @Override
    public void activate(int x, int y) {
        super.activate(x, y);
        if(enabled) {
            buttonAction.onPress();
        }
    }
}
