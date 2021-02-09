package Hilligans.Client.Rendering.Widgets;

import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Client.Rendering.World.StringRenderer;
import Hilligans.ClientMain;
import org.lwjgl.opengl.GL11;

public class SliderWidget extends Widget {

    public int value;
    int minValue;
    int maxValue;

    SliderChange sliderChange;


    public SliderWidget(int x, int y, int width, int height, int minValue, int maxValue, SliderChange sliderChange) {
        super(x, y, width, height);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.value = (minValue + maxValue) / 2;
        this.sliderChange = sliderChange;
    }

    public SliderWidget(int x, int y, int width, int height, int minValue, int maxValue, int startValue, SliderChange sliderChange) {
        super(x, y, width, height);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.value = startValue;
        this.sliderChange = sliderChange;
    }

    @Override
    public void render(MatrixStack matrixStack) {
        super.render(matrixStack);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        Renderer.drawTexture1(matrixStack, ClientMain.outLine,x,y,width,height);
        StringRenderer.drawString(matrixStack, value + "",x,y,0.5f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    @Override
    public void activate(int x, int y) {
        super.activate(x, y);
        int delta = maxValue - minValue;
        float percentage = (float)x / width;
        value = (int) (minValue + percentage * delta);
        sliderChange.onChange(value);
    }
}
