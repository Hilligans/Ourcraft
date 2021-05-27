package Hilligans.Client.Rendering.Widgets;

import Hilligans.Client.Lang.Languages;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Client.Rendering.Textures;
import Hilligans.Client.Rendering.World.StringRenderer;
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

    public ToggleWidget isEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public void render(MatrixStack matrixStack, int xOffset, int yOffset) {
        super.render(matrixStack, xOffset, yOffset);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        if(enabled) {
            Renderer.drawTexture(matrixStack, Textures.BUTTON, x, y, width, height);
        } else {
            Renderer.drawTexture(matrixStack, Textures.BUTTON_DARK,x,y,width,height);
        }
        StringRenderer.drawString(matrixStack, Languages.getTranslated(name) + (value ? " true" : " false"),x,y,0.5f);
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
