package dev.hilligans.ourcraft.client.rendering.widgets;

import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.lang.Languages;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
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
    public void render(RenderWindow window, GraphicsContext graphicsContext, MatrixStack matrixStack, int xOffset, int yOffset) {
        super.render(window, graphicsContext, matrixStack, xOffset, yOffset);

        if(isFocused && window.getClient().mouseHandler.mousePressed) {
            DoubleBuffer mousePos = window.getClient().getMousePos();
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
       // Textures.SLIDER_BACKGROUND.drawTexture(matrixStack,x - height / 4,y,width + height / 2,height);
       // Textures.SLIDER.drawTexture(matrixStack,getSliderX(height),y,height/2,height);
        window.getStringRenderer().drawStringTranslated(window, graphicsContext, matrixStack, Languages.getTranslated(name) + ": " + value,x,y,0.5f);
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