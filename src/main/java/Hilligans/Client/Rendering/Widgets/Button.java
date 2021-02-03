package Hilligans.Client.Rendering.Widgets;

import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.World.Renderer;
import Hilligans.Client.Rendering.World.StringRenderer;
import Hilligans.ClientMain;
import org.lwjgl.opengl.GL11;

public class Button extends Widget {

    ButtonAction buttonAction;
    String name;

    public Button(int x, int y, int width, int height, String name, ButtonAction buttonAction) {
        super(x, y, width, height);
        this.name = name;
        this.buttonAction = buttonAction;
    }

    @Override
    public void render(MatrixStack matrixStack) {
        super.render(matrixStack);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        Renderer.drawTexture1(ClientMain.outLine,x,y,width,height);
        StringRenderer.drawString(name,x,y,0.5f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    @Override
    public void activate() {
        super.activate();
        buttonAction.onPress();
    }
}
