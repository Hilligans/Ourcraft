package Hilligans.Client.Rendering.Widgets;

import Hilligans.Client.Client;
import Hilligans.Client.Lang.Languages;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Client.Rendering.Textures;
import Hilligans.Client.Rendering.World.StringRenderer;
import Hilligans.ClientMain;
import Hilligans.Util.Settings;
import org.lwjgl.opengl.GL11;

import java.nio.DoubleBuffer;

public class SliderWidget extends Widget {

    public int value;
    int minValue;
    int maxValue;

    SliderChange sliderChange;


    public SliderWidget(String name, int x, int y, int width, int height, int minValue, int maxValue, SliderChange sliderChange) {
        super(x, y, width, height);
        this.name = name;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.value = (minValue + maxValue) / 2;
        this.sliderChange = sliderChange;
    }

    public SliderWidget(String name, int x, int y, int width, int height, int minValue, int maxValue, int startValue, SliderChange sliderChange) {
        super(x, y, width, height);
        this.name = name;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.value = startValue;
        this.sliderChange = sliderChange;
    }

    @Override
    public void render(MatrixStack matrixStack, int xOffset, int yOffset) {
        super.render(matrixStack, xOffset, yOffset);

        if(isFocused && ClientMain.getClient().mouseHandler.mousePressed) {
            DoubleBuffer mousePos = ClientMain.getClient().getMousePos();
            if(isInBoundsX((int)mousePos.get(0))) {
                activate((int)mousePos.get(0) - x, (int)mousePos.get(1) - y);
            } else if(mousePos.get(0) - x <= 0) {
                value = minValue;
                sliderChange.onChange(value);
            } else if(mousePos.get(0) - x >= width) {
                value = maxValue;
                sliderChange.onChange(value);
            }
        }

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        Textures.SLIDER_BACKGROUND.drawTexture(matrixStack,x - height / 4,y,width + height / 2,height);
        Textures.SLIDER.drawTexture(matrixStack,getSliderX(height),y,height/2,height);
        StringRenderer.drawStringTranslated(matrixStack, Languages.getTranslated(name) + ": " + value,x,y,0.5f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    public int getSliderX(int width) {
        float delta = maxValue - minValue;
        float percentage = (value - minValue) / delta;
        return (int) (percentage * this.width) + this.x - width / 4;
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
